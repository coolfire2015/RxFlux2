package com.huyingbao.demo.util.okhttp;

import android.text.TextUtils;

import com.alibaba.sdk.android.man.MANServiceProvider;
import com.alibaba.sdk.android.man.network.MANNetworkErrorCodeBuilder;
import com.alibaba.sdk.android.man.network.MANNetworkErrorInfo;
import com.alibaba.sdk.android.man.network.MANNetworkPerformanceHitBuilder;
import com.huyingbao.demo.BuildConfig;
import com.huyingbao.demo.constant.Constants;
import com.huyingbao.demo.model.RxHttpException;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by liujunfeng on 2017/8/9.
 */
public class HostSelectionInterceptor implements Interceptor {
    private volatile String mHostUrl;

    public void setHostUrl(String hostUrl) {
        mHostUrl = hostUrl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder();
        builder.addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Accept", "application/json;charset=UTF-8")
                .addHeader("X-Requested-With", "XMLHttpRequest");
        //设置hostUrl地址
        if (!TextUtils.isEmpty(mHostUrl)) {
            HttpUrl newHttpUrl = HttpUrl.parse("http://" + mHostUrl + ":10000");
            builder.url(oldRequest.url().newBuilder()
                    .scheme(newHttpUrl.scheme())
                    .host(newHttpUrl.host())
                    .port(newHttpUrl.port())
                    .build());
        }
        //创建Request
        Request request = builder.build();
        //创建打点数据
        MANNetworkPerformanceHitBuilder networkPerformanceHitBuilder = new MANNetworkPerformanceHitBuilder(request.url().host(), request.method());
        try {
            //打点记录网络请求开始
            networkPerformanceHitBuilder.hitRequestStart();
            //发起请求时间Logger.e(String.format("发送请求 %s", response.request().url()));
            long t1 = System.nanoTime();
            //调用接口,返回数据
            Response response = chain.proceed(request);
            //打点记录网络请求结束
            networkPerformanceHitBuilder.hitRequestEndWithLoadBytes(response.body().contentLength());
            //接口数据正常
            if (response.code() == Constants.SUCCESS_CODE) {
                // 不打印日志并且数据正常直接返回
                if (!BuildConfig.LOG_DEBUG) return response;
                String content = response.body().string();
                long t2 = System.nanoTime();
                Logger.e(String.format("接收 for %s in %.1fms", response.request().url(), (t2 - t1) / 1e6d));
                Logger.json(content);
                return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();
            }
            //接口数据异常，抛出异常
            throw new RxHttpException(response.code(), response.body().string());
        } catch (retrofit2.HttpException e) {
            int code = e.code();
            if (code >= 400 && code < 500) {
                MANNetworkErrorInfo errorInfo = MANNetworkErrorCodeBuilder.buildHttpCodeClientError4XX().withExtraInfo("error_url", request.url().toString());
                networkPerformanceHitBuilder.hitRequestEndWithError(errorInfo);
            }
            if (code >= 500) {
                MANNetworkErrorInfo errorInfo = MANNetworkErrorCodeBuilder.buildHttpCodeServerError5XX().withExtraInfo("error_url", request.url().toString());
                networkPerformanceHitBuilder.hitRequestEndWithError(errorInfo);
            }
            throw e;
        } catch (MalformedURLException e) {
            MANNetworkErrorInfo errorInfo = MANNetworkErrorCodeBuilder.buildMalformedURLException().withExtraInfo("error_url", request.url().toString());
            networkPerformanceHitBuilder.hitRequestEndWithError(errorInfo);
            throw e;
        } catch (SocketTimeoutException e) {
            MANNetworkErrorInfo errorInfo = MANNetworkErrorCodeBuilder.buildSocketTimeoutException().withExtraInfo("error_url", request.url().toString());
            networkPerformanceHitBuilder.hitRequestEndWithError(errorInfo);
            throw e;
        } catch (InterruptedIOException e) {
            MANNetworkErrorInfo errorInfo = MANNetworkErrorCodeBuilder.buildInterruptedIOException().withExtraInfo("error_url", request.url().toString());
            networkPerformanceHitBuilder.hitRequestEndWithError(errorInfo);
            throw e;
        } catch (IOException e) {
            MANNetworkErrorInfo errorInfo = MANNetworkErrorCodeBuilder.buildIOException().withExtraInfo("error_url", request.url().toString());
            networkPerformanceHitBuilder.hitRequestEndWithError(errorInfo);
            throw e;
        } finally {
            MANServiceProvider.getService().getMANAnalytics().getDefaultTracker().send(networkPerformanceHitBuilder.build());
        }
    }
}

package com.huyingbao.rxflux2.util.okhttp;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.IOException;

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
        //发起请求时间Logger.e(String.format("发送请求 %s", response.request().url()));
        long t1 = System.nanoTime();
        //调用接口,返回数据
        Response response = chain.proceed(request);
        //接口数据正常
        String content = response.body().string();
        long t2 = System.nanoTime();
        Logger.e(String.format("接收 for %s in %.1fms", response.request().url(), (t2 - t1) / 1e6d));
        Logger.json(content);
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();
    }
}

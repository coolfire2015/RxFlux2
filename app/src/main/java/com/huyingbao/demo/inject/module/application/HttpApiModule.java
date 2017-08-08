package com.huyingbao.demo.inject.module.application;

import com.google.gson.GsonBuilder;
import com.huyingbao.demo.BuildConfig;
import com.huyingbao.demo.core.api.HttpApi;
import com.huyingbao.demo.utils.ServerUtils;
import com.huyingbao.demo.utils.okhttp.PersistentCookieStore;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module是一个依赖的制造工厂
 * Created by Liu Junfeng on 2017/1/1.
 */
@Module
public class HttpApiModule {
    /**
     * 创建一个HybApi的实现类单例对象
     *
     * @param client OkHttpClient
     * @return mHttpApi
     */
    @Provides
    @Singleton//添加@Singleton标明该方法产生只产生一个实例
    public HttpApi provideClientApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUtils.getServerApi())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                //.addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(HttpApi.class);
    }

    /**
     * OkHttp客户端单例对象
     *
     * @param cookieJar
     * @return OkHttpClient
     */
    @Provides
    @Singleton//添加@Singleton标明该方法产生只产生一个实例
    public OkHttpClient provideClient(CookieJar cookieJar, Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .build();
    }

    @Provides
    @Singleton//添加@Singleton标明该方法产生只产生一个实例
    public Interceptor provideLoggingInterceptor() {
        return chain -> {
            long t1 = System.nanoTime();
            //Logger.e(String.format("发送请求 %s", response.request().url()));
            Response response = chain.proceed(chain.request());
            //不打印日志并且数据正常直接返回
            if (!BuildConfig.LOG_DEBUG) return response;
            String content = response.body().string();
            long t2 = System.nanoTime();
            Logger.e(String.format("接收 for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, content));
            Logger.json(content);
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();
        };
    }

    /**
     * 本地cookie缓存单例对象
     *
     * @return
     */
    @Provides
    @Singleton//添加@Singleton标明该方法产生只产生一个实例
    public CookieJar provideCookieJar(PersistentCookieStore cookieStore) {
        CookieJar cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies != null && cookies.size() > 0)
                    for (Cookie item : cookies)
                        cookieStore.add(url, item);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
        return cookieJar;
    }
}
//    Request request = chain.request().newBuilder().addHeader("X-Requested-With", "XMLHttpRequest").build();
//                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
//                .cache(cache)
//        //云端响应头拦截器，用来配置缓存策略
//        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
//            Request request = chain.request();
//            if (!NetUtils.hasNetwork()) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build();
//                Logger.w("no network");
//            }
//            Response originalResponse = chain.proceed(request);
//            if (NetUtils.hasNetwork()) {
//                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
//                String cacheControl = request.cacheControl().toString();
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
//                        .build();
//            } else {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
//                        .removeHeader("Pragma")
//                        .build();
//            }
//        };
//
//        //缓存设置
//        File cacheFile = new File(BaseApplication.getInstance().getCacheDir(), "hybNetCache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

//日志拦截器单例对象,用于OkHttp层对日志进行处理
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
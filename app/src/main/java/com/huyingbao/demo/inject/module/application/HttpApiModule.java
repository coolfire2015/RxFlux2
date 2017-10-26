package com.huyingbao.demo.inject.module.application;

import com.google.gson.GsonBuilder;
import com.huyingbao.demo.api.HttpApi;
import com.huyingbao.demo.util.ServerUtils;
import com.huyingbao.demo.util.okhttp.HostSelectionInterceptor;
import com.huyingbao.demo.util.okhttp.PersistentCookieStore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module是一个依赖的制造工厂
 * Created by liujunfeng on 2017/1/1.
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
    @Singleton // 添加@Singleton标明该方法产生只产生一个实例
    public OkHttpClient provideClient(CookieJar cookieJar, HostSelectionInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .build();
    }

    @Provides
    @Singleton // 添加@Singleton标明该方法产生只产生一个实例
    public HostSelectionInterceptor provideHostSelectionInterceptor() {
        return new HostSelectionInterceptor();
    }

    /**
     * 本地cookie缓存单例对象
     *
     * @return
     */
    @Provides
    @Singleton // 添加@Singleton标明该方法产生只产生一个实例
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
                return cookies != null ? cookies : new ArrayList<>();
            }
        };
        return cookieJar;
    }
}
//            Request request = chain.request().newBuilder()
//                    .addHeader("Content-Type", "application/json;charset=UTF-8")
//                    .addHeader("Accept", "application/json;charset=UTF-8")
//                    .addHeader("X-Requested-With", "XMLHttpRequest")
//                    .build();
//            long t1 = System.nanoTime();
//            // Logger.e(String.format("发送请求 %s", response.request().url()));
//            //调用接口,返回数据
//            Response response = chain.proceed(request);
//            //接口session过期,创建新的数据
//            if ("true".equals(response.header("sessionTimeout"))) {
//                HttpResponse httpResponse = new HttpResponse(Constants.ERROR_SESSION_TIMEOUT, "登录过期，请重新登录！");
//                String content = JSONObject.toJSONString(httpResponse);
//                response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();
//            }

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
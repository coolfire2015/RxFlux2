package com.huyingbao.rxflux2.inject.module.application;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;
import com.huyingbao.rxflux2.api.HttpApi;
import com.huyingbao.rxflux2.util.ServerUtils;
import com.huyingbao.rxflux2.util.okhttp.HttpInterceptor;
import com.huyingbao.rxflux2.util.okhttp.PersistentCookieStore;

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
    @Singleton // 添加@Singleton标明该方法产生只产生一个实例
    public HttpApi provideClientApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUtils.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
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
    public OkHttpClient provideClient(CookieJar cookieJar, HttpInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .build();
    }

    @Provides
    @Singleton // 添加@Singleton标明该方法产生只产生一个实例
    public HttpInterceptor provideHttpInterceptor() {
        return new HttpInterceptor();
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

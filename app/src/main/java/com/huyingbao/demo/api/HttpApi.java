package com.huyingbao.demo.api;

import com.huyingbao.demo.model.GitHubRepo;
import com.huyingbao.demo.model.GitUser;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public interface HttpApi {
    @GET("/repositories")
    Observable<ArrayList<GitHubRepo>> getRepositories();

    @GET("/users/{id}")
    Observable<GitUser> getUser(@Path("id") String userId);
}

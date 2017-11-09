package com.huyingbao.rxflux2.api;

import com.huyingbao.simple.main.model.GitRepo;
import com.huyingbao.simple.main.model.GitUser;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * add:添加
 * get:获取
 * update:修改
 * delete:删除
 * apply:申请
 * confirm:确认
 * write:发送
 * check:检查
 * Created by liujunfeng on 2017/1/1.
 */
public interface HttpApi {
    @GET("/repositories")
    Observable<ArrayList<GitRepo>> getRepositories();

    @GET("/users/{id}")
    Observable<GitUser> getUser(@Path("id") int userId);
}

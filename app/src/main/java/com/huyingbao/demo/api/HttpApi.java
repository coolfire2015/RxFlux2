package com.huyingbao.demo.api;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * add:添加
 * get:获取
 * update:修改
 * delete:删除
 * apply:申请
 * confirm:确认
 * write:发送
 * check:检查
 * Created by Liu Junfeng on 2017/1/1.
 */
public interface HttpApi {
    String GET_UP_TOKEN = "getUpToken";//获取token

    @FormUrlEncoded
    @POST("/qiniu/getUpToken")
    Observable<String> getUpToken(@Field("partName") String partName);
}

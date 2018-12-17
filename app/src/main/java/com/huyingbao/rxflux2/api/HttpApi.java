package com.huyingbao.rxflux2.api;

import com.huyingbao.simple.model.GanResponse;
import com.huyingbao.simple.model.Product;

import io.reactivex.Observable;
import retrofit2.http.GET;

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
    @GET("random/data/福利/2000 ")
    Observable<GanResponse<Product>> getProductList();
}

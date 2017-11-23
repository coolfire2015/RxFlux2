package com.huyingbao.rxflux2.api;

import android.support.v4.util.ArrayMap;

import com.huyingbao.rxflux2.models.Base64File;
import com.huyingbao.rxflux2.core.empinfo.model.EmpInfo;
import com.huyingbao.rxflux2.core.imgscan.event.UploadImageResult;
import com.huyingbao.rxflux2.core.login.model.LoginInfo;
import com.huyingbao.rxflux2.models.HttpResponse;
import com.huyingbao.rxflux2.net.HttpApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.http.Body;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * add:添加 get:获取 update:修改 delete:删除 apply:申请 confirm:确认 write:发送 check:检查
 * Created by liujunfeng on 2017/1/1.
 */
public class TestHttpApi implements HttpApi {

    @Override
    public Observable<HttpResponse<LoginInfo>> login(@Body ArrayMap<String, Object> partName) {
        return null;
    }

    @Override
    public Observable<HttpResponse> getSmsCode(@Body ArrayMap<String, Object> partName) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setCode("0");
        httpResponse.setMsg("dsfg");
        return Observable.just(httpResponse).delay(1, TimeUnit.SECONDS);
    }

    @Override
    public Observable<HttpResponse> logout() {
        return null;
    }

    @Override
    public Observable<HttpResponse<EmpInfo>> getEmpInfo(@Body ArrayMap<String, Object> partName) {
        return null;
    }

    @Override
    public Observable<HttpResponse<List<Base64File>>> getEmpHead(@Body ArrayMap<String, Object> partName) {
        return null;
    }

    @Override
    public Observable<HttpResponse<UploadImageResult>> uploadMultipleFiles(@PartMap ArrayMap<String, String> files) {
        return null;
    }
}

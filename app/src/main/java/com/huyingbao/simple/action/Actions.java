package com.huyingbao.simple.action;

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
public interface Actions {
    String GET_GIT_REPO_LIST = "get_product_list";
    void getProductList();
}

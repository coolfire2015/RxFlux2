package com.huyingbao.demo.util;

/**
 * 服务器工具类
 * Created by liujunfeng on 2017/1/1.
 */

public class ServerUtils {
    private static String SERVER_API = "https://api.github.com";

    /**
     * 获取api完整地址
     *
     * @return
     */
    public static String getServerApi() {
        return SERVER_API;
    }
}

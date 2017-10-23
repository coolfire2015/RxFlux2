package com.huyingbao.demo.constant;

/**
 * 常量以及intent或者action中传递数据用的key
 * Created by liujunfeng on 2017/1/1.
 */
public interface ActionsKeys {
    String SKIP = "skip";
    String UUID = "uuid";
    String ACCOUNT = "account";//登陆用户名
    String PASSWORD = "password";
    String LOGIN_NAME = "login_name";
    String CHANNEL_ID = "channel_id";
    String STATUS_LOGOUT = "status_logout";

    String LOCATION = "location";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
    String CURRENT_POSITION = "current_position";

    String SHOP = "shop";
    String SHOP_LIST = "shop_list";
    String SHOP_ID = "shopId";
    String SHOP_TYPE = "shopType";

    String PRODUCT = "product";
    String PRODUCT_LIST = "product_list";
    String PRODUCT_STATUS = "productStatus";


    String PART_NAME = "part_name";
    String UP_TOKEN = "up_token";
    String LOCAL_PATH = "local_path";
    String FILE_KEY = "file_key";
    String FILE_KEY_LIST = "file_key_list";


    String RADIUS = "radius";
    String MSG_FROM_USER = "msg_from_user";
    String MSG_FROM_USER_LIST = "msg_from_user_list";

    String STATUS = "status";
    String RESPONSE = "response";
    String OPTIONS = "options";

    String NOTICE = "notice";
    String PUSH_MESSAGE = "push_message";


    String IS_FIRST = "is_first";//是否第一次启动
    String IS_LOGIN = "is_login";//是否已经登录

    String PUBLIC_REPOS = "repos";
    String USER = "user";
    String ID = "id";
    String SERVER_STATE = "server_state";
}

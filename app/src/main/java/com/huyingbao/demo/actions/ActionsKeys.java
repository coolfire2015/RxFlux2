package com.huyingbao.demo.actions;

/**
 * 常量以及intent或者action中传递数据用的key
 * Created by Liu Junfeng on 2017/1/1.
 */
public interface ActionsKeys {
    String PART_NAME_HEAD_IMAGE = "head";
    String URL_HEAD_IMAGE = "http://7xwebb.com1.z0.glb.clouddn.com/";

    String[] PRODUCT_TYPE = {"上衣", "裤子", "衬衣"};
    String[] PRODUCT_COLOR = {"白色", "黑色", "红色", "蓝色", "黄色", "卡其色", "灰色", "粉色"};

    String SKIP = "skip";

    String USER = "user";
    String ID = "id";
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
}

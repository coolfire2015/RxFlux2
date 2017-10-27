package com.huyingbao.rxflux2.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.util.List;

/**
 * 本地配出存贮类,保存 int,boolean,String,Object,List
 * Created by liujunfeng on 2017/1/1.
 */
public class LocalStorageUtils {
    private static final String SETTING_NAME = "Setting";
    private static SharedPreferences sSharedPreferences;
    private static LocalStorageUtils sInstance;

    private LocalStorageUtils() {
        sSharedPreferences = AppUtils.getApplication().getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
        sInstance = this;
    }

    public static LocalStorageUtils getInstance() {
        if (sInstance == null) {
            synchronized (LocalStorageUtils.class) {
                if (sInstance == null)
                    sInstance = new LocalStorageUtils();
            }
        }
        return sInstance;
    }

    public void setInt(String key, int value) {
        sSharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return sSharedPreferences.getInt(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        sSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sSharedPreferences.getBoolean(key, defaultValue);
    }

    public void setString(String key, String value) {
        sSharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sSharedPreferences.getString(key, defaultValue);
    }

    public void setLong(String key, long value) {
        sSharedPreferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return sSharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 保存对象
     *
     * @param key
     * @param t
     */
    public <T> void setObject(String key, T t) {
        sSharedPreferences.edit().putString(key, JSON.toJSONString(t)).apply();
    }

    /**
     * 获取对象
     *
     * @param key
     * @param cls
     * @return
     */
    public <T> T getObject(String key, Class<T> cls) {
        String value = sSharedPreferences.getString(key, null);
        try {
            return JSON.parseObject(value, cls);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * 保存list
     *
     * @param key
     * @param list
     */
    public <T> void setList(String key, List<T> list) {
        String value;
        try {
            value = JSON.toJSONString(list);
        } catch (JSONException e) {
            value = null;
        }
        sSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 获取对象
     *
     * @param key
     * @param cls
     * @return
     */
    public <T> List<T> getList(String key, Class<T> cls) {
        String value = sSharedPreferences.getString(key, null);
        try {
            return JSON.parseArray(value, cls);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void removeKey(String key) {
        sSharedPreferences.edit().remove(key).apply();
    }

    /**
     * 清空
     */
    public void clear() {
        sSharedPreferences.edit().clear().apply();
    }
}

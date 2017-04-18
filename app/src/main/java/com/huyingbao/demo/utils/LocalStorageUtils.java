package com.huyingbao.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 本地配出存贮类,保存 int,boolean,String,Object,List
 * Created by Liu Junfeng on 2017/1/1.
 */
public class LocalStorageUtils {

    private static final String SETTING_NAME = "Setting";
    private static SharedPreferences sSharedPreferences;
    private static LocalStorageUtils sInstance;
    private static DB sSnappyDB;
    private Context mContext;

    private LocalStorageUtils(Context context) {
        sSharedPreferences = context.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
        mContext = context;
        sInstance = this;
    }

    public static LocalStorageUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LocalStorageUtils.class) {
                if (sInstance == null)
                    sInstance = new LocalStorageUtils(context);
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
        try {
            initSnappyDb();
            if (sSnappyDB.exists(key)) {
                if (t == null) {//删除
                    sSnappyDB.del(key);
                } else {//更新
                    sSnappyDB.put(key, t);
                }
            } else {
                if (t != null) {//增加
                    sSnappyDB.put(key, t);
                }
            }
            sSnappyDB.close();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param key
     * @param cls
     * @return
     */
    public <T> T getObject(String key, Class<T> cls) {
        if (!initSnappyDb()) return null;
        try {
            T t = sSnappyDB.getObject(key, cls);
            sSnappyDB.close();
            return t;
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存list
     *
     * @param key
     * @param list
     */
    public <T> void setList(String key, List<T> list) {
        try {
            initSnappyDb();
            if (sSnappyDB.exists(key)) {
                if (list == null || list.size() == 0) {//删除
                    sSnappyDB.del(key);
                } else {//更新
                    sSnappyDB.put(key, list.toArray());
                }
            } else if (list != null && list.size() > 0) {//增加
                sSnappyDB.put(key, list.toArray());
            }
            sSnappyDB.close();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param key
     * @param cls
     * @return
     */
    public <T> List<T> getList(String key, Class<T> cls) {
        List<T> dataList = new ArrayList<T>();
        if (!initSnappyDb()) return dataList;
        try {
            if (!sSnappyDB.exists(key)) {
                sSnappyDB.close();
                return dataList;
            }
            T[] array = sSnappyDB.getObjectArray(key, cls);
            sSnappyDB.close();
            dataList.addAll(Arrays.asList(array));
            return dataList;
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return dataList;
    }


    /**
     * 获取(key:1,key:2,key:3)这种形式的list数据,例如好友列表
     *
     * @param key
     * @param cls
     * @return
     */
    public <T> List<T> getListByKey(String key, Class<T> cls) {
        List<T> dataList = new ArrayList<T>();
        if (!initSnappyDb()) return dataList;
        try {
            String[] keys = sSnappyDB.findKeys(key);
            if (key == null || key.length() == 0) {
                sSnappyDB.close();
                return dataList;
            }
            for (String item : keys) {
                dataList.add(sSnappyDB.getObject(item, cls));
            }
            sSnappyDB.close();
            return dataList;
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 清空
     */
    public void clear() {
        if (sSnappyDB != null) {
            try {
                sSnappyDB.destroy();
            } catch (SnappydbException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean initSnappyDb() {
        try {
            if (sSnappyDB != null && sSnappyDB.isOpen()) return true;
            sSnappyDB = DBFactory.open(mContext);
            return true;
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return false;
    }
}

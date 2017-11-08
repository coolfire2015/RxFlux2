package com.huyingbao.rxflux2.action;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

/**
 * Object class that hold the mType of action and the mData we want to attach to it
 */
public class RxAction {
    private final String mType;
    private final ArrayMap<String, Object> mData;

    RxAction(@NonNull String type, ArrayMap<String, Object> data) {
        this.mType = type;
        this.mData = data;
    }

    public String getType() {
        return mType;
    }

    public ArrayMap<String, Object> getData() {
        return mData;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String tag) {
        return (T) mData.get(tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RxAction)) return false;
        RxAction rxAction = (RxAction) obj;
        if (!mType.equals(rxAction.mType)) return false;
        return !(mData != null ? !mData.equals(rxAction.mData) : rxAction.mData != null);
    }

    @Override
    public int hashCode() {
        int result = mType.hashCode();
        result = 31 * result + (mData != null ? mData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RxAction{" + "mType='" + mType + '\'' + ", mData=" + mData + '}';
    }

    public static class Builder {
        private String mType;
        private ArrayMap<String, Object> mData;


        public Builder(@NonNull String type) {
            this.mType = type;
            this.mData = new ArrayMap<>();
        }


        /**
         * 向RxAction中添加key-value
         *
         * @param key   不能为空
         * @param value 为空时，key和value都不添加到action中
         * @return
         */
        public Builder put(@NonNull String key, Object value) {
            if (value != null) mData.put(key, value);
            return this;
        }

        /**
         * 生成RxAction对象
         *
         * @return
         */
        public RxAction build() {
            return new RxAction(mType, mData);
        }
    }
}
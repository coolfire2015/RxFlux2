package com.hardsoftstudio.rxflux.action;

import android.support.v4.util.ArrayMap;

/**
 * Object class that hold the mType of action and the mData we want to attach to it
 */
public class RxAction {
    private final String mType;
    private final ArrayMap<String, Object> mData;

    RxAction(String type, ArrayMap<String, Object> data) {
        this.mType = type;
        this.mData = data;
    }

    public static Builder type(String type) {
        return new Builder().with(type);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RxAction)) return false;

        RxAction rxAction = (RxAction) o;
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

        Builder with(String type) {
            if (type == null)
                throw new IllegalArgumentException("Type may not be null.");
            this.mType = type;
            this.mData = new ArrayMap<>();
            return this;
        }

        public Builder bundle(String key, Object value) {
            if (key == null)
                throw new IllegalArgumentException("Key may not be null.");
            if (value != null) mData.put(key, value);
            return this;
        }

        public RxAction build() {
            if (mType == null || mType.isEmpty())
                throw new IllegalArgumentException("At least one key is required.");
            return new RxAction(mType, mData);
        }
    }
}
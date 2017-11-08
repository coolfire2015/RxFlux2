package com.huyingbao.rxflux2.action

import android.support.v4.util.ArrayMap

/**
 * Object class that hold the mType of action and the mData we want to attach to it
 */
open class RxAction internal constructor(val type: String, val data: ArrayMap<String, Any>?) {

    operator fun <T> get(tag: String): T {
        return data!![tag] as T
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj !is RxAction) return false
        val rxAction = obj as RxAction?
        return if (type != rxAction!!.type) false else !if (data != null) data != rxAction.data else rxAction.data != null
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "RxAction{mType='$type', mData=$data}"
    }

    class Builder(private val mType: String) {
        private val mData: ArrayMap<String, Any>


        init {
            this.mData = ArrayMap()
        }


        /**
         * 向RxAction中添加key-value
         *
         * @param key   不能为空
         * @param value 为空时，key和value都不添加到action中
         * @return
         */
        fun put(key: String, value: Any?): Builder {
            if (value != null) mData.put(key, value)
            return this
        }

        /**
         * 生成RxAction对象
         *
         * @return
         */
        fun build(): RxAction {
            return RxAction(mType, mData)
        }
    }
}
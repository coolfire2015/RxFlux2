package com.huyingbao.rxflux2.action

import android.support.v4.util.ArrayMap

/**
 * Object class that hold the mType of action and the mData we want to attach to it
 */
open class RxAction internal constructor(val type: String, val data: ArrayMap<String, Any>?) {

    operator fun <T> get(tag: String): T {
        return data!![tag] as T
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RxAction) return false

        val rxAction = o as RxAction?
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

    class Builder {
        private var mType: String? = null
        private var mData: ArrayMap<String, Any>? = null

        internal fun with(type: String?): Builder {
            if (type == null)
                throw IllegalArgumentException("Type may not be null.")
            this.mType = type
            this.mData = ArrayMap()
            return this
        }

        fun bundle(key: String?, value: Any?): Builder {
            if (key == null)
                throw IllegalArgumentException("Key may not be null.")
            if (value != null) mData!!.put(key, value)
            return this
        }

        fun build(): RxAction {
            if (mType == null || mType!!.isEmpty())
                throw IllegalArgumentException("At least one key is required.")
            return RxAction(mType, mData)
        }
    }

    companion object {
        fun type(type: String): Builder {
            return Builder().with(type)
        }
    }
}
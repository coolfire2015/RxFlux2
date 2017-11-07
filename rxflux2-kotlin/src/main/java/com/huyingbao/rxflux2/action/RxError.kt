package com.huyingbao.rxflux2.action

import android.support.v4.util.ArrayMap

/**
 * Special RxAction used for Errors, use the RxError.newRxError(action, throwable) to create a
 * new instance
 */
class RxError private constructor(type: String, data: ArrayMap<String, Any>) : RxAction(type, data) {

    val action: RxAction
        get() = data!![ERROR_ACTION] as RxAction

    val throwable: Throwable
        get() = data!![ERROR_THROWABLE] as Throwable

    companion object {

        private val ERROR_TYPE = "RxError_Type"

        private val ERROR_ACTION = "RxError_Action"

        private val ERROR_THROWABLE = "RxError_Throwable"

        fun newRxError(action: RxAction, throwable: Throwable): RxError {
            val data = ArrayMap<String, Any>()
            data.put(ERROR_ACTION, action)
            data.put(ERROR_THROWABLE, throwable)
            return RxError(ERROR_TYPE, data)
        }
    }
}

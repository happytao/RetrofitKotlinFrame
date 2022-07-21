package com.example.kotlinflowlearn.netutil

import okhttp3.Headers
import java.io.Serializable

sealed class HttpResult<out T>(open val responseHeader: Headers?) : Serializable {
    val isSuccess: Boolean get() = this is Success

    val isFailure: Boolean get() = this !is Success

    open fun exceptionOrNull(): Throwable? = null


    data class Success<T : Any>(val value: T, override val responseHeader: Headers?) :
        HttpResult<T>(responseHeader) {
        override fun toString(): String {
            return "Success{$value}"
        }

        override fun exceptionOrNull(): Throwable? = null
    }

    data class ApiError(
        val code: Int = -1,
        val message: String? = null,
        val throwable: Throwable?,
        override val responseHeader: Headers? = null
    ) : HttpResult<Nothing>(responseHeader) {
        override fun toString(): String {
            return "ApiError(message:$message,code:$code)"
        }

        override fun exceptionOrNull(): Throwable? = throwable
    }

    data class NetworkError(
        val throwable: Throwable?,
        override val responseHeader: Headers? = null
        ): HttpResult<Nothing>(responseHeader) {
        override fun toString(): String {
            super.toString()
            return "NetworkError(Throwable:${throwable?.message})"
        }

        override fun exceptionOrNull(): Throwable? = throwable
    }

    data class UnknownError(
        val throwable: Throwable?,
        override val responseHeader: Headers? = null
    ): HttpResult<Nothing>(responseHeader) {
        override fun toString(): String {
            super.toString()
            return "UnknownError(Throwable:${throwable?.message})"
        }

        override fun exceptionOrNull(): Throwable? = throwable
    }

    fun getOrNull() : T? = (this as? Success)?.value

    companion object {
        fun <T:Any> success(result:T, responseHeader: Headers?): HttpResult<T> =
            Success(result,responseHeader)

        fun apiError(
            code:Int = -1,
            message:String? = null,
            throwable: Throwable?,
            responseHeader: Headers?
        ): HttpResult<Nothing> =
            ApiError(code,message,throwable,responseHeader)

        fun <Nothing> networkError(
            error:Throwable,responseHeader: Headers?
        ): HttpResult<Nothing> =
            NetworkError(error,responseHeader)

        fun <Nothing> unknownError(
            throwable: Throwable?,responseHeader: Headers?
        ): HttpResult<Nothing> =
            UnknownError(throwable,responseHeader)
    }


}

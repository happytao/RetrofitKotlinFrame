package com.example.kotlinflowlearn.netutil

import com.example.kotlinflowlearn.entity.BeanWrapper
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

internal class SuspendHttpCall<T : BeanWrapper<R>,R :Any>(
    private val delegate: Call<T>,
):Call<HttpResult<T>> {
    override fun clone(): Call<HttpResult<T>> = SuspendHttpCall(delegate.clone())

    override fun execute(): Response<HttpResult<T>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")

    }

    override fun enqueue(callback: Callback<HttpResult<T>>) {
        return delegate.enqueue(object :Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                var httpResult:HttpResult<T>? = null
                //==================================
                //==================================
                //===============1.响应成功==================
                //==================================
                //==================================
                if(response.isSuccessful) {
                    body?.result?.apply {
                        //result不为空
                        httpResult = HttpResult.success(body,response.headers())
                    } ?: run {
                        //响应body是null或者bean为空时
                        httpResult = HttpResult.unknownError(
                            IllegalArgumentException("response data is invalid"),
                            null
                        )
                    }

                    callback.onResponse(
                        this@SuspendHttpCall,
                        Response.success(httpResult)
                    )
                    return
                }
                //响应失败
                onFailure(call,HttpException(response))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                var reason:String? = null
                var statusCode = -1
                val result:HttpResult<T> = generateHttpResult(t,reason,statusCode)
                callback.onResponse(this@SuspendHttpCall, Response.success(result))
            }
        })
    }

    fun generateHttpResult(
        t: Throwable,
        reason:String?,
        statusCode:Int
    ): HttpResult<Nothing> {
        //如果是apiError
        if(t is HttpException) {
            return HttpResult.ApiError(
                statusCode,
                reason,
                t,
                null
            )
        }
        //如果是网络错误
        if(t is SocketTimeoutException || t is ConnectException || t is SSLHandshakeException) {
            return HttpResult.NetworkError(
                t,
                null
            )
        }

        return HttpResult.UnknownError(t,null)


    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

}
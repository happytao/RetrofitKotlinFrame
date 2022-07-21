package com.example.kotlinflowlearn.netutil

import com.example.kotlinflowlearn.entity.BeanWrapper
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class SuspendCallAdapterFactory :CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //第一个泛型就是HttpResult类，这种情况可能是api接口没有声明协程suspend符号，抛出异常提醒
        if(getRawType(returnType) == HttpResult::class.java) {
            throw IllegalArgumentException("Method must be declare suspend,please check function declaration at API interface")
        }
        //协程挂起函数默认返回值是Call<*> 如果不满足，返回null让retrofit的其他来处理
        if(Call::class.java != getRawType(returnType)) {
            return null
        }

        check(returnType is ParameterizedType) {
            "return type must be HttpResult<*> or HttpResult<out *> for Call<*> check"
        }
        //获取Call类包裹的第一个泛型
        val responseType = getParameterUpperBound(0,returnType)
        //如果Call之后的泛型还不是HttpResult则返回空让其他方法处理
        if(getRawType(responseType) != HttpResult::class.java) {
            return null
        }
        //确保HttpResult内部包的泛型其还包裹另外一层泛型，比如 HttpResult<*>
        check(responseType is ParameterizedType) {
            "return type must be HttpResult<*> or HttpResult<out *> for HttpResult<*> check"
        }
        //获取HttpResult包裹的第一个泛型
        val successBodyType = getParameterUpperBound(0,responseType)


        return SuspendCallAdapter<BeanWrapper<Any>,Any>(
        successBodyType
        )


    }

    companion object {

        fun create():SuspendCallAdapterFactory{
            return SuspendCallAdapterFactory()

        }
    }
}

class SuspendCallAdapter<T: BeanWrapper<R>, R: Any>(
    private val successType: Type,
):CallAdapter<T,Call<HttpResult<T>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<HttpResult<T>> {
        return SuspendHttpCall(call)
    }

}
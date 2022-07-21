package com.example.kotlinflowlearn.api

import com.example.kotlinflowlearn.netutil.SuspendCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val apiClient:WeatherApi by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { getInstance() }


    private fun getInstance():WeatherApi {
        var retrofit = Retrofit.Builder()
            .baseUrl("http://apis.juhe.cn/")
            .client(OkHttpWrapper.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SuspendCallAdapterFactory.create())
            .build()
        return retrofit.create(WeatherApi::class.java)
    }
}
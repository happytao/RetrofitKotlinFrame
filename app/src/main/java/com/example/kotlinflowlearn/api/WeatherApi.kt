package com.example.kotlinflowlearn.api

import com.example.kotlinflowlearn.entity.BeanWrapper
import com.example.kotlinflowlearn.entity.CityBean
import com.example.kotlinflowlearn.entity.WeatherBean
import com.example.kotlinflowlearn.netutil.HttpResult
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("simpleWeather/query")
    suspend fun getWeatherByCity(
        @Query("city") city: String,
        @Query("key") key: String
    ):HttpResult<BeanWrapper<WeatherBean>>


    @GET("simpleWeather/cityList")
    suspend fun getCityList(
        @Query("key") key:String
    ):HttpResult<BeanWrapper<MutableList<CityBean>>>


}
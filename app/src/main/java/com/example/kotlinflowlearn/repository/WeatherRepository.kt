package com.example.kotlinflowlearn.repository

import com.example.kotlinflowlearn.api.ApiClient
import com.example.kotlinflowlearn.entity.BeanWrapper
import com.example.kotlinflowlearn.entity.CityBean
import com.example.kotlinflowlearn.entity.WeatherBean
import com.example.kotlinflowlearn.netutil.HttpResult

class WeatherRepository {
    companion object {
        private const val KEY = "b0eabbf4ca6768b2530d2368da5fa788"
        suspend fun getWeatherByCity(city:String):HttpResult<BeanWrapper<WeatherBean>> {
            return ApiClient.apiClient.getWeatherByCity(city, KEY)
        }

        suspend fun getCityList():HttpResult<BeanWrapper<MutableList<CityBean>>> {
            return ApiClient.apiClient.getCityList(KEY)
        }
    }
}
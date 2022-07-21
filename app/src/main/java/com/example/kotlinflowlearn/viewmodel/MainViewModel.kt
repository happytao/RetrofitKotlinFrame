package com.example.kotlinflowlearn.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinflowlearn.entity.BeanWrapper
import com.example.kotlinflowlearn.entity.CityBean
import com.example.kotlinflowlearn.entity.PlusBean
import com.example.kotlinflowlearn.entity.WeatherBean
import com.example.kotlinflowlearn.logutil.logE
import com.example.kotlinflowlearn.netutil.HttpResult
import com.example.kotlinflowlearn.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {
    val liveWeatherBean = MutableLiveData<WeatherBean>()
    val livePlusBean = MutableLiveData<MutableList<PlusBean>>()
    var plusBeanList: ArrayList<PlusBean>? = null
    var weatherList = ArrayList<WeatherBean.Future>()
    var cityList = ArrayList<CityBean>()

    fun getWeatherByCity(city: String) {

        viewModelScope.launch {


            var nowTime = System.currentTimeMillis()
            var weatherBean = getWeatherListAsync(city).await()
            when(weatherBean) {
                is HttpResult.ApiError -> weatherBean.throwable.logE()
                is HttpResult.NetworkError -> weatherBean.throwable.logE()
                is HttpResult.Success -> {
                    var dealWeatherBean = weatherBean.value
                    dealWeatherBean?.let { weatherList.addAll(it.result.future) }
                    liveWeatherBean.postValue(dealWeatherBean.result)
                }
                is HttpResult.UnknownError -> weatherBean.throwable.logE()
            }



            var time1 = System.currentTimeMillis() - nowTime

            var cityBeanList = getCityListAsync().await()
            when(cityBeanList) {
                is HttpResult.ApiError -> cityBeanList.throwable.logE()
                is HttpResult.NetworkError -> cityBeanList.throwable.logE()
                is HttpResult.Success -> {
                    var dealCityBeanList = cityBeanList.value
                    cityBeanList?.let { cityList.addAll(dealCityBeanList.result) }
                }
                is HttpResult.UnknownError -> cityBeanList.throwable.logE()
            }


            var time2 = System.currentTimeMillis() - time1
            var totalTime = System.currentTimeMillis() - nowTime


            Log.d("timeCost", "f1:$time1 + f2:$time2")


            Log.d("timeCost", "totalTime:$totalTime")



            zipWeatherAndCityBean()
            livePlusBean.postValue(plusBeanList)
        }


    }

    private fun getWeatherListAsync(city: String) = viewModelScope.async<HttpResult<BeanWrapper<WeatherBean>>> {
        WeatherRepository.getWeatherByCity(city)
    }


    private fun getCityListAsync() = viewModelScope.async<HttpResult<BeanWrapper<MutableList<CityBean>>>> {
        WeatherRepository.getCityList()
    }

    private fun zipWeatherAndCityBean() {
        plusBeanList = ArrayList(cityList.size)
        cityList.forEachIndexed { index, cityBean ->
            if (index < weatherList.size) {
                plusBeanList!!.add(
                    PlusBean(
                        date = weatherList[index].date,
                        direct = weatherList[index].direct,
                        temperature = weatherList[index].temperature,
                        weather = weatherList[index].weather,
                        wid = weatherList[index].wid,
                        city = cityBean.city,
                        province = cityBean.province,
                        district = cityBean.district,
                        id = cityBean.id
                    )
                )
            } else {
                plusBeanList!!.add(
                    PlusBean(
                        date = null,
                        direct = null,
                        temperature = null,
                        weather = null,
                        wid = null,
                        city = cityBean.city,
                        province = cityBean.province,
                        district = cityBean.district,
                        id = cityBean.id
                    )
                )
            }


        }
    }

}
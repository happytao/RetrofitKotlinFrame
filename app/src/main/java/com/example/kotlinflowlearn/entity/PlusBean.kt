package com.example.kotlinflowlearn.entity

data class PlusBean(
    var date: String?,
    var direct: String?,
    var temperature: String?,
    var weather: String?,
    var wid: WeatherBean.Future.Wid?,
    var city: String?,
    var district: String?,
    var id: String?,
    var province: String?
)

package com.example.kotlinflowlearn.entity

data class WeatherBean(
    var city: String,
    var future: List<Future>,
    var realtime: Realtime
) {
    data class Future(
        var date: String,
        var direct: String,
        var temperature: String,
        var weather: String,
        var wid: Wid
    ) {
        data class Wid(
            var day: String,
            var night: String
        )
    }

    data class Realtime(
        var aqi: String,
        var direct: String,
        var humidity: String,
        var info: String,
        var power: String,
        var temperature: String,
        var wid: String
    )
}
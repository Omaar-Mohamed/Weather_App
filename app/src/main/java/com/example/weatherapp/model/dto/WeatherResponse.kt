package com.example.weatherapp.model.dto

import com.fasterxml.jackson.annotation.JsonProperty


data class WeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @JsonProperty("timezone_offset")
    val timezoneOffset: Long,
    val current: Current,
    val minutely: List<Minutely>,
    val hourly: List<Hourly>,
    val daily: List<Daily>,
    val alerts: List<Alert>,
)

data class Current(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    @JsonProperty("feels_like")
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    @JsonProperty("dew_point")
    val dewPoint: Double,
    val uvi: Double, // Change the type to Double
    val clouds: Long,
    val visibility: Long,
    @JsonProperty("wind_speed")
    val windSpeed: Double,
    @JsonProperty("wind_deg")
    val windDeg: Long,
    val weather: List<Weather>,
)


data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
)

data class Minutely(
    val dt: Long,
    val precipitation: Long,
)

data class Hourly(
    val dt: Long,
    val temp: Double,
    @JsonProperty("feels_like")
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    @JsonProperty("dew_point")
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    @JsonProperty("wind_speed")
    val windSpeed: Double,
    @JsonProperty("wind_deg")
    val windDeg: Long,
    @JsonProperty("wind_gust")
    val windGust: Double,
    val weather: List<Weather2>,
    val pop: Double, // Change the type to Double
)

data class Weather2(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
)

data class Daily(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    @JsonProperty("moon_phase")
    val moonPhase: Double,
    val summary: String,
    val temp: Temp,
    @JsonProperty("feels_like")
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    @JsonProperty("dew_point")
    val dewPoint: Double,
    @JsonProperty("wind_speed")
    val windSpeed: Double,
    @JsonProperty("wind_deg")
    val windDeg: Long,
    @JsonProperty("wind_gust")
    val windGust: Double,
    val weather: List<Weather3>,
    val clouds: Long,
    val pop: Double,
    val uvi: Double,
    val rain: Double?,
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,
)

data class Weather3(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
)

data class Alert(
    @JsonProperty("sender_name")
    val senderName: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    val tags: List<String>,
)

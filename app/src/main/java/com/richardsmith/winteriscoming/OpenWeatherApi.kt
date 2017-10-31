package com.richardsmith.winteriscoming

import com.squareup.moshi.Json
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by richardsmith on 2017-10-23.
 */

data class Weatherr(val id: Int, val main: String, val description: String)
data class Main(val temperature: Float, val humidity: Int, val temp_min: Float, val temp_max)
data class Clouds(val all: Int)
data class Coordinates(@Json(name = "lon") val longitude: Float,
                       @Json(name = "lat") val latitude: Float)
data class WeatherResponse(@Json(name = "coord") val coordinates: Coordinates,
                           val weatherr: Weatherr, val main: Main, val clouds: Clouds, val id: Int, val name: String)

interface OpenWeatherApi {
    @GET("data/2.5/weather")
    fun loadWeather(@Query("name") cityName: String,
                    @Query("units") unit: String,
                    @Query("appid") apiKey: String): Call<WeatherResponse>
}

class WeatherRepository {
    companion object {
        val API_KEY = "04091fd66df531a795449eff064a3ad1"
    }
    private val openWeatherApi: OpenWeatherApi

    init {
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        openWeatherApi = retrofit.create(OpenWeatherApi::class.java)
    }
    fun fetchCurrentWeather(cityName: String): Call<WeatherResponse> {
        return openWeatherApi.loadWeather(cityName, "metric", API_KEY)
    }
}




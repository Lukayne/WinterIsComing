package com.richardsmith.winteriscoming

import java.util.*

/**
 * Created by richardsmith on 2017-10-11.
 */

data class WeatherResult(
        val name: String,
        val main: Main,
        val weather: List<Weather>
)

data class Weather(
        val description: String,
        val main: String
)












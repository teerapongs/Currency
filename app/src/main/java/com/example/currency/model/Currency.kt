package com.example.currency.model

data class Currency(
    val time: Time,
    val disclaimer: String,
    val chartName: String,
    val bpi: BPI
)

data class Time(
    val updated: String,
    val updatedISO: String,
    val updateduk: String
)

data class BPI(
    val USD: Detail,
    val GBP: Detail,
    val EUR: Detail
)

data class Detail(
    val code: String,
    val symbol: String,
    val rate: String,
    val description: String,
    val rate_float: Float
)
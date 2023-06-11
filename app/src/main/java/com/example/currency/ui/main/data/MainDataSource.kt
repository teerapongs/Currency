package com.example.currency.ui.main.data

import com.example.currency.service.CurrencyService

class MainDataSource(private val service: CurrencyService) {

    suspend fun getCurrency() = service.getCurrency()

}
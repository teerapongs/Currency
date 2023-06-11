package com.example.currency.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currency.service.CurrencyService
import com.example.currency.ui.main.data.MainDataSource
import com.example.currency.ui.main.data.MainRepository

class MainViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                repository = MainRepository(
                    dataSource = MainDataSource(
                        service = CurrencyService.create()
                    )
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
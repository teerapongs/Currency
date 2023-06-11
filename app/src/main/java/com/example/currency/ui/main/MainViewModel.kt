package com.example.currency.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.R
import com.example.currency.extensions.isFloat
import com.example.currency.model.Currency
import com.example.currency.model.ResultResponse
import com.example.currency.ui.main.data.MainRepositoryInterface
import com.example.currency.ui.main.model.MainFormState
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepositoryInterface): ViewModel() {


    private val _convertForm = MutableLiveData<MainFormState>()
    val convertForm: LiveData<MainFormState> = _convertForm

    private val _convertToBTC = MutableLiveData<String>()
    val convertToBTC: LiveData<String> = _convertToBTC

    private val _getCurrencySuccessLiveData = MutableLiveData<ResultResponse<Currency>>()
    val getCurrencySuccess: LiveData<ResultResponse<Currency>> = _getCurrencySuccessLiveData

    init {
        getCurrency()
    }
    fun getCurrency() {
        viewModelScope.launch {
            repository.getCurrency().collect { resource ->
                when(resource.success != null) {
                    true -> {
                        _getCurrencySuccessLiveData.value = ResultResponse(success = resource.success)
                    }
                    else -> {
                        _getCurrencySuccessLiveData.value = ResultResponse(error = resource.error, errorInt = resource.errorInt)
                    }
                }
            }
        }
    }

    fun convertCurrencyChanged(currency: String) {
        if (currency.isEmpty()) {
            _convertForm.value = MainFormState(currencyError = R.string.required_currency)
        } else if (!currency.isFloat()) {
            _convertForm.value = MainFormState(currencyError = R.string.can_not_convert_to_number)
        } else{
            _convertForm.value = MainFormState(isDataValid = true)
        }
    }

    fun convertToBTC(rate: Float, _rateCurrency: String) {
        if (rate != 0F && _rateCurrency.isNotEmpty()) {
            val rateCurrency = if (_rateCurrency.isNotEmpty()) _rateCurrency.toFloat() else 0.0F
            val convert = rateCurrency / rate
            _convertToBTC.value = if (convert.isNaN()) "0.0" else "$convert"
        }
    }
}
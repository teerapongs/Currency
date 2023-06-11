package com.example.currency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.currency.model.BPI
import com.example.currency.model.Currency
import com.example.currency.model.Detail
import com.example.currency.model.ResultResponse
import com.example.currency.model.Time
import com.example.currency.ui.main.MainViewModel
import com.example.currency.ui.main.data.FakeMainRepositoryInterface
import com.example.currency.ui.main.model.MainFormState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Date

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var viewModel: MainViewModel
    private lateinit var fakeMainRepositoryInterface: FakeMainRepositoryInterface

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        fakeMainRepositoryInterface = FakeMainRepositoryInterface()
        viewModel = MainViewModel(fakeMainRepositoryInterface)
    }

    @Test
    fun mainViewModelConvertCorrect() {
        val rate = 25000F
        val rateCurrency = "25000"
        val convertLiveData = MutableLiveData<String>()
        viewModel.convertToBTC(rate, rateCurrency)
        convertLiveData.value = viewModel.convertToBTC.value
        val value = convertLiveData.value.toString().toFloat()
        assertEquals(1F, value)
    }

    @Test
    fun mainViewModelConvertCurrencyOnChangedCorrect() {
        val currency = "26000.00"
        val convertFormLiveData = MutableLiveData<MainFormState>()
        viewModel.convertCurrencyChanged(currency)
        convertFormLiveData.value = viewModel.convertForm.value
        val value = convertFormLiveData.value
        assertEquals(true, value?.isDataValid)
    }

    @Test
    fun mainViewModelGetCurrencyError() = runTest {
        val currency = MutableLiveData<ResultResponse<Currency>>()
        fakeMainRepositoryInterface.emit(ResultResponse(error = "Error"))
        currency.value = viewModel.getCurrencySuccess.value
        val value = currency.value
        val error = value?.error
        assertThat(value?.success).isNull()
        assertEquals("Error",error)
    }

    @Test
    fun mainViewModelGetCurrencySuccess() = runTest {
        val currency = MutableLiveData<ResultResponse<Currency>>()
        val mockCurrency = Currency(
            time = Time(updateduk = "${Date()}", updatedISO = "${Date()}", updated = "${Date()}"),
            disclaimer = "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
            chartName = "Bitcoin",
            bpi = BPI(
                USD = Detail(code = "USD", symbol = "&#36", rate = "12345", description = "United States Dollar", rate_float = 12345F),
                GBP = Detail(code = "GBP", symbol = "&pound", rate = "12345", description = "British Pound Sterling", rate_float = 12345F),
                EUR = Detail(code = "EUR", symbol = "&#euro", rate = "12345", description = "Euro", rate_float = 12345F),
            )
        )
        fakeMainRepositoryInterface.emit(ResultResponse(success = mockCurrency))
        currency.value = viewModel.getCurrencySuccess.value
        val value = currency.value
        val error = value?.error
        assertThat(value?.success).isNotNull()
        assertEquals(null,error)
    }
}

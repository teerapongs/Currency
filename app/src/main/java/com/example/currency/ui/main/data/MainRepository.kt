package com.example.currency.ui.main.data

import com.example.currency.R
import com.example.currency.model.Currency
import com.example.currency.model.ResultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface MainRepositoryInterface {
    fun getCurrency(): Flow<ResultResponse<Currency>>
}

class FakeMainRepositoryInterface: MainRepositoryInterface {

    private val fakeFlow = MutableSharedFlow<ResultResponse<Currency>>()
    suspend fun emit(value: ResultResponse<Currency>) = fakeFlow.emit(value = value)
    override fun getCurrency() = fakeFlow

}

class MainRepository(private val dataSource: MainDataSource): MainRepositoryInterface {

    override fun getCurrency() = flow {
        try {
            val response = dataSource.getCurrency()
            if (response.isSuccessful) {
                val result = response.body()
                emit(ResultResponse(success = result))
            } else {
                emit(ResultResponse(errorInt = R.string.http_url_connection_failed))
            }
        } catch (e: IOException) {
            emit(ResultResponse(error = e.message))
        } catch (e: Throwable) {
            emit(ResultResponse(error = e.message))
        } catch (e: UnknownHostException) {
            emit(ResultResponse(error = e.message))
        } catch (e: SocketTimeoutException) {
            emit(ResultResponse(error = e.message))
        } catch (e: Exception) {
            emit(ResultResponse(error = e.message))
        }
    }
}
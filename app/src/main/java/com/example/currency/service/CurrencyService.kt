package com.example.currency.service

import com.example.currency.model.Currency
import com.ihsanbal.logging.LoggingInterceptor
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface CurrencyService {

    @GET(CURRENCY)
    suspend fun getCurrency(): Response<Currency>

    companion object {
        private const val TIME_OUT = 90L

        fun create(): CurrencyService {
            val loggerInterceptor = LoggingInterceptor.Builder()
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            okHttpClientBuilder
                .addInterceptor(loggerInterceptor.build())
                .certificatePinner(CertificatePinner.DEFAULT)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                    val newRequest: Request = requestBuilder.apply {
                        header("Content-Type", "application/json")
                    }.build()
                    chain.proceed(newRequest)
                }
            okHttpClientBuilder.build()
            val url = BASE_URL
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
            return retrofit.create(CurrencyService::class.java)
        }
    }
}
package com.example.onlinesales.webServices

import android.util.Log
import com.example.onlinesales.webServices.Constant.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class RetrofitService {

    companion object Factory {
        var retrofitCall: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitCall == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitCall = retrofit.create(RetrofitService::class.java)
            }
            return retrofitCall!!
        }

        fun create(isHeader: Boolean, token: String): RetrofitAPI {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(30, TimeUnit.SECONDS)
            httpClient.writeTimeout(30, TimeUnit.SECONDS)
            httpClient.readTimeout(30, TimeUnit.SECONDS)
            httpClient.addInterceptor(interceptor)
            if (isHeader) {
                Log.e("Token", token)
                httpClient.addInterceptor(Interceptor { chain ->
                    var builder = chain.request()
                    try {
                        builder = chain.request().newBuilder()
                            .header("Authorization", "Bearer $token")
                            .header("Accept", "application/json")
                            .build()
                    } catch (exception: java.lang.Exception) {
                        when (exception) {
                            is SocketTimeoutException -> {
                            }

                            is SocketException -> {}
                            is IOException -> {}
                            else -> exception.stackTrace
                        }
                    }
                    chain.proceed(builder)
                })


            } else {
                httpClient.addInterceptor(object : Interceptor {
                    @Throws(java.io.IOException::class)

                    override fun intercept(chain: Interceptor.Chain): Response {
                        var builder = chain.request()
                        try {
                            builder = chain.request().newBuilder()
                                .header("Accept", "application/json")
                                .build()
                        } catch (exception: Exception) {
                            when (exception) {
                                is SocketTimeoutException -> {
                                }
                                is SocketException -> {}
                                is java.io.IOException -> {}
                                else -> exception.stackTrace
                            }
                        }
                        return chain.proceed(builder)
                    }

                })
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RetrofitAPI::class.java)
        }
    }






}
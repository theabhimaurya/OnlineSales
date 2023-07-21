package com.example.onlinesales.webServices

import com.example.onlinesales.model.RequestModel
import com.example.onlinesales.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST

interface RetrofitAPI {

    @POST("v4")
    suspend fun solve(
        @Body requestModel: RequestModel
    )
    :ResponseModel
}
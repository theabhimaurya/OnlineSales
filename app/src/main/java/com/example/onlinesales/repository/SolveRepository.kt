package com.example.onlinesales.repository

import com.example.onlinesales.model.RequestModel
import com.example.onlinesales.model.ResponseModel
import com.example.onlinesales.webServices.ApiState
import com.example.onlinesales.webServices.RetrofitAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SolveRepository(private val apiService: RetrofitAPI) {

    suspend fun solve(requestModel : RequestModel): Flow<ApiState<ResponseModel>> {
        return flow {
            val checklist = apiService.solve(requestModel)
            emit(ApiState.success(checklist))
        }.flowOn(Dispatchers.IO)
    }
}
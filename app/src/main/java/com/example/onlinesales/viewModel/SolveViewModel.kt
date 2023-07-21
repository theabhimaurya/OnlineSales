package com.example.onlinesales.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinesales.model.RequestModel
import com.example.onlinesales.model.ResponseModel
import com.example.onlinesales.repository.SolveRepository
import com.example.onlinesales.webServices.ApiState
import com.example.onlinesales.webServices.RetrofitService
import com.example.onlinesales.webServices.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SolveViewModel : ViewModel() {

    private val repository= SolveRepository(RetrofitService.create(false,""))

    val solveResponse = MutableStateFlow(ApiState(Status.NONE,ResponseModel(),""))


    fun solve(requestModel: RequestModel){
        solveResponse.value = ApiState.loading()
        viewModelScope.launch {
            repository.solve(requestModel)
                .catch {
                    solveResponse.value = ApiState.error(it.message.toString())
                }
                .collect{
                    solveResponse.value = ApiState.success(it.data)
                }

        }
    }


}
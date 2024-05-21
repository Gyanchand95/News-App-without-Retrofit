package com.mynewsapp.network

import com.mynewsapp.model.NewsResponse

sealed class ApiCallState {
    object Loading : ApiCallState()
    data class Success(val data: NewsResponse) : ApiCallState()
    data class Error(val error: String) : ApiCallState()
}
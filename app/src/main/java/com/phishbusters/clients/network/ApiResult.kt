package com.phishbusters.clients.network

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception? = null) : ApiResult<Nothing>()
}

package com.phishbusters.clients.network

import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

enum class HttpMethod {
    GET, POST, PUT, PATCH, DELETE
}


class ApiService {
    val client = OkHttpClient()
    val gson = Gson()
    val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    inline fun <reified T> httpCallWithPayload(
        url: String,
        method: HttpMethod,
        payload: Map<String, Any?>,
        crossinline onResponse: (ApiResult<T>) -> Unit
    ) {
        val jsonBody = gson.toJson(payload)
        httpCall(url, method, jsonBody, onResponse)
    }

    inline fun <reified T> httpCall(
        url: String,
        method: HttpMethod,
        jsonBody: String? = null,
        crossinline onResponse: (ApiResult<T>) -> Unit
    ) {
        val builder = Request.Builder().url(url)
        jsonBody?.let {
            val body = it.toRequestBody(jsonMediaType)
            when (method) {
                HttpMethod.POST -> builder.post(body)
                HttpMethod.PUT -> builder.put(body)
                HttpMethod.PATCH -> builder.patch(body)
                HttpMethod.DELETE -> builder.delete(body)
                HttpMethod.GET -> builder.get()
            }
        } ?: run {
            when (method) {
                HttpMethod.GET -> builder.get()
                HttpMethod.DELETE -> builder.delete()
                else -> throw IllegalArgumentException("Body must not be null for $method")
            }
        }

        val request = builder.build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResponse(ApiResult.Error(e))
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string()
                    val result: T = gson.fromJson(body, T::class.java)
                    onResponse(ApiResult.Success(result))
                } catch (e: Exception) {
                    onResponse(ApiResult.Error(e))
                }
            }
        })
    }
}

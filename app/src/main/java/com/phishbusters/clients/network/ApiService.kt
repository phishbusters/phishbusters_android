package com.phishbusters.clients.network

import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiService {
    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun httpCall(url: String, method: String, jsonBody: String?, callback: Callback) {
        val builder = Request.Builder()
            .url(url)

        if (jsonBody != null) {
            val body = jsonBody.toRequestBody(jsonMediaType)
            when (method) {
                "POST" -> builder.post(body)
                "PUT" -> builder.put(body)
                "PATCH" -> builder.patch(body)
                "DELETE" -> builder.delete(body)
            }
        } else {
            when (method) {
                "GET" -> builder.get()
                "DELETE" -> builder.delete()
            }
        }

        val request = builder.build()

        client.newCall(request).enqueue(callback)
    }
}

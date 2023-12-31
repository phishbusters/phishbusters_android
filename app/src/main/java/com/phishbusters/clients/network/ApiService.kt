package com.phishbusters.clients.network

import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
import com.phishbusters.clients.data.ConfigVars
import com.phishbusters.clients.data.TokenStore
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

enum class HttpMethod {
    GET, POST, PUT, PATCH, DELETE
}


class ApiService(val tokenStore: TokenStore) {
    var client: OkHttpClient
    val gson = Gson()
    val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    init {
        if (ConfigVars.DEV_ENV == "DEV") {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) =
                    Unit

                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) =
                    Unit
            })

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val allTrustingSocketFactory = sslContext.socketFactory

            client = OkHttpClient.Builder()
                .sslSocketFactory(allTrustingSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()
        } else {
            client = OkHttpClient()
        }
    }

    inline fun <reified T> httpCallWithPayload(
        url: String,
        method: HttpMethod,
        payload: Map<String, Any?>,
        crossinline onResponse: (ApiResult<T>) -> Unit
    ) {
        val jsonBody = gson.toJson(payload)
        httpCall<T>(url, method, jsonBody = jsonBody, onResponse = onResponse)
    }

    inline fun <reified T> httpCall(
        url: String,
        method: HttpMethod,
        type: Type = T::class.java,
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

        tokenStore.getToken()?.let { token ->
            builder.addHeader("Authorization", "Bearer $token")
        }

        val request = builder.build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val errorResult = when (e) {
                    is SocketTimeoutException -> ApiResult.Error(
                        e,
                        "Tiempo de espera agotado, verifica tu conexión"
                    )

                    else -> ApiResult.Error(e, "Error de red")
                }

                onResponse(errorResult)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        val result: T = gson.fromJson(body, type)
                        onResponse(ApiResult.Success(result))
                    } else {
                        print("Error en el back: ${response.code} body: ${response.body?.string()}")
                    }
                } catch (e: Exception) {
                    onResponse(ApiResult.Error(e))
                }
            }
        })
    }

    suspend inline fun <reified T> suspendHttpCall(
        url: String,
        method: HttpMethod,
        type: Type = T::class.java,
        jsonBody: String? = null,
    ): ApiResult<T> = suspendCancellableCoroutine { continuation ->
        try {
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

            tokenStore.getToken()?.let { token ->
                builder.addHeader("Authorization", "Bearer $token")
            }

            val request = builder.build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWith(Result.success(ApiResult.Error(e)))
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result: T = gson.fromJson(body, type)
                            continuation.resumeWith(Result.success(ApiResult.Success(result)))
                        } else {
                            continuation.resumeWith(Result.success(ApiResult.Error(Exception("Error en el back: ${response.code} body: ${response.body?.string()}"))))
                        }
                    } catch (e: Exception) {
                        continuation.resumeWith(Result.success(ApiResult.Error(e)))
                    }
                }
            })
        } catch (e: SocketTimeoutException) {
            continuation.resumeWith(
                Result.success(
                    ApiResult.Error(
                        e,
                        "Tiempo de espera agotado, verifica tu conexión"
                    )
                )
            )
        } catch (e: IOException) {
            continuation.resumeWith(Result.success(ApiResult.Error(e, "Error de red")))
        } catch (e: Exception) {
            continuation.resumeWith(Result.success(ApiResult.Error(e, "Error desconocido")))
        }
    }
}

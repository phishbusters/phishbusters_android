package com.phishbusters.clients.data

import android.content.Context
import android.content.SharedPreferences

class TokenStore(private val context: Context) {
    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        // TODO: When logging is finished remove second argument
        return sharedPreferences.getString("jwt_token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImZwbWlyYWJpbGVAZ21haWwuY29tIiwiaWF0IjoxNjkyOTM0NjU0fQ.AddF4dWKqplGNs7n0XE2SQVhGW2TgyVQkgj4SEKh7MM")
    }
}

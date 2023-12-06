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
        return sharedPreferences.getString("jwt_token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InNlZ3VyaWRhZEBwaGlzaGJ1c3RlcnMuY29tIiwiaWF0IjoxNjk4NTM2ODkyfQ.El_UdJTe55M0s4LQHpAtsVRoSjVKY7k_SsSRHqw7VL4")
    }
}

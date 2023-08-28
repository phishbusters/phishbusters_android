package com.phishbusters.clients.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    fun setTutorialCompleted() {
        sharedPreferences.edit().putBoolean("tutorial_completed", true).apply()
    }

    fun isTutorialCompleted(): Boolean {
        return sharedPreferences.getBoolean("tutorial_completed", false)
    }
}
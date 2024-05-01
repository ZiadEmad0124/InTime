package com.ziad_emad_dev.in_time.network

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    companion object {
        private const val USER_TOKEN = "User Token"
    }

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("InTime", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun isAuthEmpty(): Boolean {
        return sharedPreferences.getString(USER_TOKEN, null) == null
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }
}
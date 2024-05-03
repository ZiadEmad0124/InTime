package com.ziad_emad_dev.in_time.network.auth

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    companion object {
        private const val ACCESS_TOKEN = "Access Token"
        private const val REFRESH_TOKEN = "Refresh Token"
    }

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("InTime", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
    }

    fun saveRefreshToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(REFRESH_TOKEN, token)
        editor.apply()
    }

    fun isAuthEmpty(): Boolean {
        return sharedPreferences.getString(ACCESS_TOKEN, null) == null
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }
}
package com.ziad_emad_dev.in_time.network.auth

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    companion object {
        private const val ACCESS_TOKEN = "Access Token"
        private const val REFRESH_TOKEN = "Refresh Token"
    }

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("Session", Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
    }

    fun saveRefreshToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(REFRESH_TOKEN, token)
        editor.apply()
    }

    fun isAccessTokenEmpty(): Boolean {
        return sharedPreferences.getString(ACCESS_TOKEN, null) == null
    }

    fun fetchAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    fun fetchRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    fun clearAccessToken() {
        val editor = sharedPreferences.edit()
        editor.remove(ACCESS_TOKEN)
        editor.apply()
    }

    fun clearRefreshToken() {
        val editor = sharedPreferences.edit()
        editor.remove(REFRESH_TOKEN)
        editor.apply()
    }
}
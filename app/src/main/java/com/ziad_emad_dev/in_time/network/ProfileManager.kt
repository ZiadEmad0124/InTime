package com.ziad_emad_dev.in_time.network

import android.content.Context

class ProfileManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Profile", Context.MODE_PRIVATE)

    fun saveProfile(name: String, title: String, email: String, phone: String, about: String) {
        sharedPreferences.edit().apply {
            putString("name", name)
            putString("title", title)
            putString("email", email)
            putString("phone", phone)
            putString("about", about)
            apply()
        }
    }

    fun getProfileName(): String {
        return sharedPreferences.getString("name", "") ?: ""
    }

    fun getProfileTitle(): String {
        return sharedPreferences.getString("title", "") ?: ""
    }

    fun getProfileEmail(): String {
        return sharedPreferences.getString("email", "") ?: ""
    }

    fun getProfilePhone(): String {
        return sharedPreferences.getString("phone", "") ?: ""
    }

    fun getProfileAbout(): String {
        return sharedPreferences.getString("about", "") ?: ""
    }
}
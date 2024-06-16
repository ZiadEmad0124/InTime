package com.ziad_emad_dev.in_time.network.profile

import android.content.Context

class ProfileManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Profile", Context.MODE_PRIVATE)

    fun saveProfile(name: String, title: String, email: String, phone: String, avatar: String, about: String, totalPoints: Int) {
        sharedPreferences.edit().apply {
            putString("name", name)
            putString("title", title)
            putString("email", email)
            putString("phone", phone)
            putString("avatar", avatar)
            putString("about", about)
            putInt("totalPoints", totalPoints)
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
        return "0${sharedPreferences.getString("phone", "")}"
    }

    fun getProfileAvatar(): String {
        return sharedPreferences.getString("avatar", "") ?: ""
    }

    fun getProfileAbout(): String {
        return sharedPreferences.getString("about", "") ?: ""
    }

    fun getTotalPoints(): Int {
        return sharedPreferences.getInt("totalPoints", 0)
    }
}
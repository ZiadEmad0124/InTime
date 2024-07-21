package com.ziad_emad_dev.in_time.network.profile

import android.content.Context

class ProfileManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Profile", Context.MODE_PRIVATE)

    fun saveProfile(id: String, name: String, email: String, phone: String, avatar: String, title: String,
                    about: String, totalPoints: Int, completedTasks: Int, onGoingTasks: Int) {
        sharedPreferences.edit().apply {
            putString("id", id)
            putString("name", name)
            putString("email", email)
            putString("phone", phone)
            putString("avatar", avatar)
            putString("title", title)
            putString("about", about)
            putInt("totalPoints", totalPoints)
            putInt("completedTasks", completedTasks)
            putInt("onGoingTasks", onGoingTasks)
            apply()
        }
    }

    fun saveProfileRank(rank: Int) {
        sharedPreferences.edit().apply {
            putInt("rank", rank)
            apply()
        }
    }

    fun getProfileId(): String {
        return sharedPreferences.getString("id", "") ?: ""
    }

    fun getProfileName(): String {
        return sharedPreferences.getString("name", "") ?: ""
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

    fun getProfileTitle(): String {
        return sharedPreferences.getString("title", "") ?: ""
    }

    fun getProfileAbout(): String {
        return sharedPreferences.getString("about", "") ?: ""
    }

    fun getTotalPoints(): Int {
        return sharedPreferences.getInt("totalPoints", 0)
    }

    fun getCompletedTasks(): Int {
        return sharedPreferences.getInt("completedTasks", 0)
    }

    fun getOnGoingTasks(): Int {
        return sharedPreferences.getInt("onGoingTasks", 0)
    }

    fun getRank(): Int {
        return sharedPreferences.getInt("rank", 0)
    }

    fun clearProfile() {
        sharedPreferences.edit().clear().apply()
    }
}
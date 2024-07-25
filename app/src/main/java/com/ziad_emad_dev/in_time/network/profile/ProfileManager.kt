package com.ziad_emad_dev.in_time.network.profile

import android.content.Context

class ProfileManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Profile", Context.MODE_PRIVATE)

    fun saveProfile(
        id: String, name: String, email: String, phone: String, avatar: String, title: String,
        about: String, totalPoints: Int, completedTasks: Int, onGoingTasks: Int
    ) {
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

    private fun getProfileEmail(): String {
        return sharedPreferences.getString("email", "") ?: ""
    }

    private fun getProfilePhone(): String {
        return "0${sharedPreferences.getString("phone", "")}"
    }

    fun getProfileAvatar(): String {
        return sharedPreferences.getString("avatar", "") ?: ""
    }

    private fun getProfileTitle(): String {
        return sharedPreferences.getString("title", "") ?: ""
    }

    private fun getProfileAbout(): String {
        return sharedPreferences.getString("about", "") ?: ""
    }

    private fun getRank(): Int {
        return sharedPreferences.getInt("rank", 0)
    }

    private fun getLevel(): Int {
        return ((getTotalPoints() / 100) + 1)
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

    fun getProfile(): Profile {
        return Profile(
            getProfileId(),
            getProfileName(),
            getProfileEmail(),
            getProfilePhone(),
            getProfileAvatar(),
            getProfileTitle(),
            getProfileAbout(),
            getRank().toString(),
            getLevel().toString(),
            getTotalPoints().toString(),
            getCompletedTasks().toString(),
            getOnGoingTasks().toString()
        )
    }

    fun clearProfile() {
        sharedPreferences.edit().clear().apply()
    }
}
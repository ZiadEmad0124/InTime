package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenRequest
import com.ziad_emad_dev.in_time.network.auth.sign_out.SignOutRequest
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)
    private val profileManager = ProfileManager(context)

    private val _refreshTokenMessage = MutableLiveData<String>()
    val refreshTokenMessage get() = _refreshTokenMessage

    private val _fetchProfileMessage = MutableLiveData<String>()
    val fetchProfileMessage get() = _fetchProfileMessage

    private val _signOutMessage = MutableLiveData<String>()
    val signOutMessage get() = _signOutMessage

    fun refreshToken() {

        viewModelScope.launch {
            val request = RefreshTokenRequest(sessionManager.fetchRefreshToken().toString())

            try {
                val response = InTimeApi.retrofitService.refreshToken(request)
                if (response.isSuccessful) {
                    sessionManager.saveAuthToken(response.body()?.newAccessToken.toString())
                    sessionManager.saveRefreshToken(response.body()?.newRefreshToken.toString())
                    _refreshTokenMessage.value = response.body()?.success.toString()
                } else {
                    _refreshTokenMessage.value = "Refresh Failed"
                }
            } catch (e: Exception) {
                _refreshTokenMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.fetchUser("Bearer ${sessionManager.fetchAuthToken()}")
                if (response.isSuccessful) {
                    val title = when (response.body()?.record?.title) {
                        null -> "User"
                        else -> response.body()?.record?.title.toString()
                    }
                    val about = when (response.body()?.record?.about) {
                        null -> "New User"
                        else -> response.body()?.record?.about.toString()
                    }
                    profileManager.saveProfile(
                        response.body()?.record?.name.toString(),
                        title,
                        response.body()?.record?.email.toString(),
                        response.body()?.record?.phone.toString(),
                        "https://intime-9hga.onrender.com/api/v1/images/${response.body()?.record?.avatar.toString()}",
                        about,
                        response.body()?.record?.points?.totalPoints ?: 0)
                    _fetchProfileMessage.value = response.body()?.success.toString()
                } else {
                    _fetchProfileMessage.value = "Fetch Profile Failed"
                }
            } catch (e: Exception) {
                _fetchProfileMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun signOut() {
        val request = SignOutRequest(sessionManager.fetchRefreshToken().toString())

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.signOut(request)
                if (response.isSuccessful) {
                    sessionManager.clearAuthToken()
                    sessionManager.clearRefreshToken()
                    _signOutMessage.value = response.body()?.success.toString()
                } else {
                    _signOutMessage.value = "Sign Out Failed"
                }
            } catch (e: Exception) {
                _signOutMessage.value = "Failed Connect, Try Again"
            }
        }
    }
}
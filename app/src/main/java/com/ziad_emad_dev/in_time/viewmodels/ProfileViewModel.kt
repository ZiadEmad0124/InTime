package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenRequest
import com.ziad_emad_dev.in_time.network.auth.sign_out.SignOutRequest
import kotlinx.coroutines.launch

class ProfileViewModel(context: Context) : ViewModel() {

    private val context by lazy {
        context
    }

    private val _refreshTokenMessage = MutableLiveData<String>()
    val refreshTokenMessage get() = _refreshTokenMessage

    private val _fetchProfileMessage = MutableLiveData<String>()
    val fetchProfileMessage get() = _fetchProfileMessage

    private val _signOutMessage = MutableLiveData<String>()
    val signOutMessage get() = _signOutMessage

    private val _name = MutableLiveData<String>()
    val name get() = _name

    private val _email = MutableLiveData<String>()
    val email get() = _email

    private val _phone = MutableLiveData<String>()
    val phone get() = _phone

    fun refreshToken() {

        viewModelScope.launch {
            val request = RefreshTokenRequest(SessionManager(context).fetchRefreshToken().toString())

            try {
                val response = InTimeApi.retrofitService.refreshToken(request)
                if (response.isSuccessful) {
                    SessionManager(context).saveAuthToken(response.body()?.newAccessToken.toString())
                    SessionManager(context).saveRefreshToken(response.body()?.newRefreshToken.toString())
                    _refreshTokenMessage.value = "Refresh Succeed"
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
                val response = InTimeApi.retrofitService.fetchUser("Bearer ${SessionManager(context).fetchAuthToken().toString()}")
                if (response.isSuccessful) {
                    _fetchProfileMessage.value = response.body()?.success.toString()
                    _name.value = response.body()?.record?.name
                    _email.value = response.body()?.record?.email
                    _phone.value = response.body()?.record?.phone.toString()
                } else {
                    _fetchProfileMessage.value = "Response Failed"
                }
            } catch (e: Exception) {
                _fetchProfileMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun signOut() {
        val request = SignOutRequest(SessionManager(context).fetchRefreshToken().toString())

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.signOut(request)
                if (response.isSuccessful) {
                    _signOutMessage.value = response.body()?.success.toString()
                    SessionManager(context).clearAuthToken()
                    SessionManager(context).clearRefreshToken()
                } else {
                    _signOutMessage.value = "Sign Out Failed"
                }
            } catch (e: Exception) {
                _signOutMessage.value = "Failed Connect, Try Again"
            }
        }
    }
}
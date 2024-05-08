package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenRequest
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {

    private val context by lazy {
        context
    }

    private val _refreshTokenMessage = MutableLiveData<String>()
    val refreshTokenMessage get() = _refreshTokenMessage

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
}
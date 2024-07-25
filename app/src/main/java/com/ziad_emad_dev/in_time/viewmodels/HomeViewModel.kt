package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenRequest
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenResponse
import com.ziad_emad_dev.in_time.network.auth.sign_out.SignOutRequest
import com.ziad_emad_dev.in_time.network.notification.Record
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.project.get_project.GetProjectResponse
import com.ziad_emad_dev.in_time.network.tasks.Task
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {

    companion object {
        private const val FAILED_CONNECT = "Failed Connect, Try Again"
        private const val SOMETHING_WENT_WRONG = "Something went wrong"
        private const val IMAGES_URL = "https://intime-9hga.onrender.com/api/v1/images/"
    }

    private val sessionManager = SessionManager(context)
    private val profileManager = ProfileManager(context)

    private val _refreshTokenMessage = MutableLiveData<String>()
    val refreshTokenMessage get() = _refreshTokenMessage

    fun refreshToken() {

        viewModelScope.launch {
            val request = RefreshTokenRequest(sessionManager.fetchRefreshToken().toString())

            try {
                val response = InTimeApi.retrofitService.refreshToken(request)
                if (response.isSuccessful) {
                    sessionManager.saveAccessToken(response.body()?.newAccessToken.toString())
                    sessionManager.saveRefreshToken(response.body()?.newRefreshToken.toString())
                    _refreshTokenMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorRefreshTokenResponse = Gson().fromJson(errorResponse, RefreshTokenResponse::class.java)
                    _refreshTokenMessage.value = errorRefreshTokenResponse?.message.toString()
                }
            } catch (e: Exception) {
                _refreshTokenMessage.value = FAILED_CONNECT
            }
        }
    }

    private val _fetchProfileMessage = MutableLiveData<String>()
    val fetchProfileMessage get() = _fetchProfileMessage

    private val _profileName = MutableLiveData<String>()
    val profileName get() = _profileName

    private val _profileAvatar = MutableLiveData<String>()
    val profileAvatar get() = _profileAvatar

    fun fetchProfile() {

        viewModelScope.launch {
            val response = InTimeApi.retrofitService.fetchUser("Bearer ${sessionManager.fetchAccessToken()}")

            try {
                if (response.isSuccessful) {
                    profileManager.saveProfile(
                        response.body()?.record?.id.toString(),
                        response.body()?.record?.name.toString(),
                        response.body()?.record?.email.toString(),
                        response.body()?.record?.phone.toString(),
                        IMAGES_URL + response.body()?.record?.avatar.toString(),
                        response.body()?.record?.title ?: "New User",
                        response.body()?.record?.about ?: "New User",
                        response.body()?.record?.points?.totalPoints ?: 0,
                        response.body()?.record?.tasks?.completedTasks ?: 0,
                        response.body()?.record?.tasks?.onGoingTasks ?: 0
                    )
                    _fetchProfileMessage.value = response.body()?.success.toString()
                    _profileName.value = response.body()?.record?.name.toString()
                    _profileAvatar.value = IMAGES_URL + response.body()?.record?.avatar.toString()
                } else {
                    _fetchProfileMessage.value = SOMETHING_WENT_WRONG
                }
            } catch (e: Exception) {
                _fetchProfileMessage.value = FAILED_CONNECT
            }
        }
    }

    private val _signOutMessage = MutableLiveData<String>()
    val signOutMessage get() = _signOutMessage

    fun signOut() {
        val request = SignOutRequest(sessionManager.fetchRefreshToken().toString())

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.signOut(request)
                if (response.isSuccessful) {
                    _signOutMessage.value = response.body()?.success.toString()
                } else {
                    _signOutMessage.value = "Sign Out Failed"
                }
            } catch (e: Exception) {
                _signOutMessage.value = FAILED_CONNECT
            }
            sessionManager.clearAccessToken()
            sessionManager.clearRefreshToken()
            profileManager.clearProfile()
        }
    }

    private val _joinProjectMessage = MutableLiveData<String>()
    val joinProjectMessage get() = _joinProjectMessage

    private val _getTasksMessage = MutableLiveData<String>()
    val getTasksMessage get() = _getTasksMessage

    private val _getTasks = MutableLiveData<List<Task>>()
    val getTasks get() = _getTasks

    private val _getSearchTask = MutableLiveData<List<Task>>()
    val getSearchTask get() = _getSearchTask

    private val _getSearchTaskMessage = MutableLiveData<String>()
    val getSearchTaskMessage get() = _getSearchTaskMessage

    private val _getNotifications = MutableLiveData<Record>()
    val getNotifications get() = _getNotifications

    private val _getNotificationsMessage = MutableLiveData<String>()
    val getNotificationsMessage get() = _getNotificationsMessage

    fun getTasks() {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getTwoTasks(
                    "Bearer ${sessionManager.fetchAccessToken().toString()}",
                    -1, false, completed = false, sortBy = "createdAt"
                )
                if (response.isSuccessful) {
                    _getTasksMessage.value = response.body()?.success.toString()
                    _getTasks.value = response.body()?.record!!
                } else {
                    _getTasksMessage.value = "Get all Tasks failed"
                }
            } catch (e: Exception) {
                _getTasksMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun joinProject(projectId: String, otp: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.joinProject(
                    "Bearer ${
                        sessionManager.fetchAccessToken().toString()
                    }", projectId, otp
                )
                if (response.isSuccessful) {
                    _joinProjectMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorSignInResponse =
                        Gson().fromJson(errorResponse, GetProjectResponse::class.java)
                    _joinProjectMessage.value = errorSignInResponse?.message.toString()
                }
            } catch (e: Exception) {
                _joinProjectMessage.value = e.message
            }
        }
    }

    fun searchTask(taskName: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.searchTasks(
                    "Bearer ${
                        sessionManager.fetchAccessToken().toString()
                    }", taskName
                )
                if (response.isSuccessful) {
                    _getSearchTask.value = response.body()?.record!!
                } else {
                    _getSearchTaskMessage.value = "Search Task failed"
                }
            } catch (e: Exception) {
                _getSearchTaskMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun getNotification() {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getNotifications(
                    "Bearer ${
                        sessionManager.fetchAccessToken().toString()
                    }"
                )
                if (response.isSuccessful) {
                    _getNotificationsMessage.value = response.body()?.success.toString()
                    _getNotifications.value = response.body()?.record!!
                } else {
                    _getNotificationsMessage.value = "Get Notification failed"
                }
            } catch (e: Exception) {
                _getNotificationsMessage.value = "Failed Connect, Try Again"
            }
        }
    }
}
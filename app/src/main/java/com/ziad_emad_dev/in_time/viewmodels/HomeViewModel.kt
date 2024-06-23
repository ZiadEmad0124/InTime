package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenRequest
import com.ziad_emad_dev.in_time.network.auth.sign_out.SignOutRequest
import com.ziad_emad_dev.in_time.network.notification.Record
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.project.get_project.GetProjectResponse
import com.ziad_emad_dev.in_time.network.tasks.Task
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)
    private val profileManager = ProfileManager(context)

    private val _refreshTokenMessage = MutableLiveData<String>()
    val refreshTokenMessage get() = _refreshTokenMessage

    private val _fetchProfileMessage = MutableLiveData<String>()
    val fetchProfileMessage get() = _fetchProfileMessage

    private val _fetchProfileRankMessage = MutableLiveData<String>()
    val fetchProfileRankMessage get() = _fetchProfileRankMessage

    private val _signOutMessage = MutableLiveData<String>()
    val signOutMessage get() = _signOutMessage

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
                        response.body()?.record?.id.toString(),
                        response.body()?.record?.name.toString(),
                        title,
                        response.body()?.record?.email.toString(),
                        response.body()?.record?.phone.toString(),
                        "https://intime-9hga.onrender.com/api/v1/images/${response.body()?.record?.avatar.toString()}",
                        about,
                        response.body()?.record?.points?.totalPoints ?: 0,
                        response.body()?.record?.tasks?.completedTasks ?: 0,
                        response.body()?.record?.tasks?.onGoingTasks ?: 0)
                    _fetchProfileMessage.value = response.body()?.success.toString()
                } else {
                    _fetchProfileMessage.value = "Fetch Profile Failed"
                }
            } catch (e: Exception) {
                _fetchProfileMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun fetchProfileRank() {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.fetchUserRank("Bearer ${sessionManager.fetchAuthToken()}")
                if (response.isSuccessful) {
                    _fetchProfileRankMessage.value = "Fetch Profile Rank Success"
                    profileManager.saveProfileRank(response.body()?.myRank ?: 0)
                }else{
                    _fetchProfileRankMessage.value = "Fetch Profile Rank Failed"
                }
            } catch (e: Exception) {
                _fetchProfileRankMessage.value = "Failed Connect, Try Again"
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

    fun getTasks() {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getTwoTasks("Bearer ${sessionManager.fetchAuthToken().toString()}",
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

    fun joinProject(link: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.joinProject("Bearer ${sessionManager.fetchAuthToken().toString()}", link)
                if (response.isSuccessful) {
                    _joinProjectMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorSignInResponse = Gson().fromJson(errorResponse, GetProjectResponse::class.java)
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
                val response = InTimeApi.retrofitService.searchTasks("Bearer ${sessionManager.fetchAuthToken().toString()}", taskName)
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
                val response = InTimeApi.retrofitService.getNotifications("Bearer ${sessionManager.fetchAuthToken().toString()}")
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
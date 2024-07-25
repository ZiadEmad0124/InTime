package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.profile.change_password.ChangePasswordRequest
import com.ziad_emad_dev.in_time.network.profile.change_password.ChangePasswordResponse
import com.ziad_emad_dev.in_time.network.profile.edit_profile.EditProfileResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileViewModel(context: Context) : ViewModel() {

    companion object {
        private const val FAILED_CONNECT = "Failed Connect, Try Again"
        private const val SOMETHING_WENT_WRONG = "Something went wrong"
        private const val IMAGES_URL = "https://intime-9hga.onrender.com/api/v1/images/"
    }

    private val sessionManager = SessionManager(context)
    private val profileManager = ProfileManager(context)

    private val _profile = MutableLiveData(profileManager.getProfile())
    val profile get() = _profile

    private val _fetchProfileRankMessage = MutableLiveData<String>()
    val fetchProfileRankMessage get() = _fetchProfileRankMessage

    fun fetchProfileRank() {

        viewModelScope.launch {
            val response = InTimeApi.retrofitService.fetchUserRank("Bearer ${sessionManager.fetchAccessToken()}")

            try {
                if (response.isSuccessful) {
                    _fetchProfileRankMessage.value = "true"
                    profileManager.saveProfileRank(response.body()?.myRank ?: 0)
                    _profile.value = profileManager.getProfile()
                } else {
                    _fetchProfileRankMessage.value = SOMETHING_WENT_WRONG
                }
            } catch (e: Exception) {
                _fetchProfileRankMessage.value = FAILED_CONNECT
            }
        }
    }

    private val _deleteAccountMessage = MutableLiveData<String>()
    val deleteAccountMessage get() = _deleteAccountMessage

    fun deleteAccount() {

        viewModelScope.launch {
            val response = InTimeApi.retrofitService.deleteAccount("Bearer ${sessionManager.fetchAccessToken().toString()}")

            try {
                if (response.isSuccessful) {
                    sessionManager.clearAccessToken()
                    sessionManager.clearRefreshToken()
                    profileManager.clearProfile()
                    _deleteAccountMessage.value = response.body()?.success.toString()
                } else {
                    _deleteAccountMessage.value = "Deleting Account Failed"
                }
            } catch (e: Exception) {
                _deleteAccountMessage.value = FAILED_CONNECT
            }
        }
    }

    private val _changePasswordMessage = MutableLiveData<String>()
    val changePasswordMessage get() = _changePasswordMessage

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {

        viewModelScope.launch {
            val request = ChangePasswordRequest(currentPassword, newPassword, confirmPassword)
            val response = InTimeApi.retrofitService.changePassword("Bearer ${sessionManager.fetchAccessToken().toString()}", request)

            try {
                if (response.isSuccessful) {
                    _changePasswordMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorChangePasswordRequest = Gson().fromJson(errorResponse, ChangePasswordResponse::class.java)
                    _changePasswordMessage.value = errorChangePasswordRequest?.message.toString()
                }
            } catch (e: Exception) {
                _changePasswordMessage.value = FAILED_CONNECT
            }
        }
    }

    private val _editProfileMessage = MutableLiveData<String>()
    val editProfileMessage get() = _editProfileMessage

    fun editProfile(name: String, title: String, phone: String, about: String, avatar: File?) {

        val myName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val myTitle = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val myPhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val myAbout = about.toRequestBody("text/plain".toMediaTypeOrNull())

        val myAvatar = avatar?.let {
            val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("avatar", it.name, requestFile)
        }

        viewModelScope.launch {
            val response = InTimeApi.retrofitService.editProfile(
                "Bearer ${sessionManager.fetchAccessToken().toString()}", myName, myTitle, myPhone, myAbout, myAvatar)

            try {
                if (response.isSuccessful) {
                    _editProfileMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorEditProfileRequest = Gson().fromJson(errorResponse, EditProfileResponse::class.java)
                    _editProfileMessage.value = errorEditProfileRequest.message.toString()
                }
            } catch (e: Exception) {
                _editProfileMessage.value = FAILED_CONNECT
            }
        }
    }

    fun removeAvatar() {

        viewModelScope.launch {
            val response = InTimeApi.retrofitService.removeAvatar("Bearer ${sessionManager.fetchAccessToken().toString()}")

            try {
                if (response.isSuccessful) {
                    _editProfileMessage.value = response.body()?.success.toString()
                } else {
                    _editProfileMessage.value = "Remove Avatar Failed"
                }
            } catch (e: Exception) {
                _editProfileMessage.value = FAILED_CONNECT
            }
        }
    }

    private val _fetchProfileMessage = MutableLiveData<String>()
    val fetchProfileMessage get() = _fetchProfileMessage

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
                    _profile.value = profileManager.getProfile()
                } else {
                    _fetchProfileMessage.value = SOMETHING_WENT_WRONG
                }
            } catch (e: Exception) {
                _fetchProfileMessage.value = FAILED_CONNECT
            }
        }
    }
}
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
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileViewModel(context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)
    private val profileManager = ProfileManager(context)

    private val _fetchProfileMessage = MutableLiveData<String>()
    val fetchProfileMessage get() = _fetchProfileMessage

    private val _deleteAccountMessage = MutableLiveData<String>()
    val deleteAccountMessage get() = _deleteAccountMessage

    private val _editProfileMessage = MutableLiveData<String>()
    val editProfileMessage get() = _editProfileMessage

    private val _changePasswordMessage = MutableLiveData<String>()
    val changePasswordMessage get() = _changePasswordMessage

    fun deleteAccount() {

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.deleteAccount("Bearer ${sessionManager.fetchAuthToken().toString()}")
                if (response.isSuccessful) {
                    sessionManager.clearAuthToken()
                    sessionManager.clearRefreshToken()
                    _deleteAccountMessage.value = response.body()?.success.toString()
                } else {
                    _deleteAccountMessage.value = "Delete Account Failed"
                }
            } catch (e: Exception) {
                _deleteAccountMessage.value = "Failed Connect, Try Again"
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
                    _fetchProfileMessage.value = "fetch profile Failed"
                }
            } catch (e: Exception) {
                _fetchProfileMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun editProfile(name: String, title: String, phone: String, about: String, imageFile: File) {

        val myName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val myTitle = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val myPhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val myAbout = about.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val avatar = MultipartBody.Part.createFormData("avatar", imageFile.name, requestFile)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.editProfile("Bearer ${sessionManager.fetchAuthToken().toString()}",
                    myName, myTitle, myPhone, myAbout, avatar)
                if (response.isSuccessful) {
                    _editProfileMessage.value = response.body()?.success.toString()
                } else {
                    _editProfileMessage.value = "Edit Profile Failed"
                }
            } catch (e: Exception) {
                _editProfileMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun editProfile(name: String, title: String, phone: String, about: String) {

        val myName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val myTitle = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val myPhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val myAbout = about.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.editProfileWithoutAvatar("Bearer ${sessionManager.fetchAuthToken().toString()}",
                    myName, myTitle, myPhone, myAbout)
                if (response.isSuccessful) {
                    _editProfileMessage.value = response.body()?.success.toString()
                } else {
                    _editProfileMessage.value = "Edit Profile Failed"
                }
            } catch (e: Exception) {
                _editProfileMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun removeAvatar() {

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.removeAvatar("Bearer ${sessionManager.fetchAuthToken().toString()}")
                if (response.isSuccessful) {
                    _editProfileMessage.value = response.body()?.success.toString()
                } else {
                    _editProfileMessage.value = "Remove Avatar Failed"
                }
            } catch (e: Exception) {
                _editProfileMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {

        viewModelScope.launch {
            val request = ChangePasswordRequest(currentPassword, newPassword, confirmPassword)

            try {
                val response = InTimeApi.retrofitService.changePassword("Bearer ${sessionManager.fetchAuthToken().toString()}", request)
                if (response.isSuccessful) {
                    _changePasswordMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorChangePasswordRequest = Gson().fromJson(errorResponse, ChangePasswordResponse::class.java)
                    _changePasswordMessage.value = errorChangePasswordRequest?.message.toString()
                }
            } catch (e: Exception) {
                _changePasswordMessage.value = "Failed Connect, Try Again"
            }
        }
    }
}
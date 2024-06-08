package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenRequest
import kotlinx.coroutines.launch

class ProfileViewModel(context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _refreshTokenMessage = MutableLiveData<String>()
    val refreshTokenMessage get() = _refreshTokenMessage

    private val _deleteAccountMessage = MutableLiveData<String>()
    val deleteAccountMessage get() = _deleteAccountMessage

//    private val _editProfileMessage = MutableLiveData<String>()
//    val editProfileMessage get() = _editProfileMessage

    fun refreshToken() {

        viewModelScope.launch {
            val request = RefreshTokenRequest(sessionManager.fetchRefreshToken().toString())

            try {
                val response = InTimeApi.retrofitService.refreshToken(request)
                if (response.isSuccessful) {
                    _refreshTokenMessage.value = "Refresh Succeed"
                    sessionManager.saveAuthToken(response.body()?.newAccessToken.toString())
                    sessionManager.saveRefreshToken(response.body()?.newRefreshToken.toString())
                } else {
                    _refreshTokenMessage.value = "Refresh Failed"
                }
            } catch (e: Exception) {
                _refreshTokenMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun deleteAccount() {

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.deleteAccount("Bearer ${sessionManager.fetchAuthToken().toString()}")
                if (response.isSuccessful) {
                    _deleteAccountMessage.value = "Deleted Succeed"
                } else {
                    _refreshTokenMessage.value = "Deleted Failed"
                }
            } catch (e: Exception) {
                _refreshTokenMessage.value = "Failed Connect, Try Again"
            }
        }
    }

//
//    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
//        val fileName = "my_image" // you can change this to make it unique for each image
//        val file = File(context.getExternalFilesDir(null), fileName)
//        val outStream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
//        outStream.close()
//        return file
//    }
//
//    fun editProfile(name: String, phone: String, age: String, imagePath: Bitmap) {
//        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
//        val phoneRequestBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
//        val ageRequestBody = age.toRequestBody("text/plain".toMediaTypeOrNull())
//
//        val file = bitmapToFile(imagePath, context)
//        val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
//        val imagePart = MultipartBody.Part.createFormData("image", file.name, imageRequestBody)
//
//        viewModelScope.launch {
//            try {
//                val response = InTimeApi.retrofitService.editProfile("Bearer ${SessionManager(context).fetchAuthToken().toString()}",
//                    nameRequestBody, phoneRequestBody, ageRequestBody, imagePart)
//                if (response.isSuccessful) {
//                    _editProfileMessage.value = response.body()?.success.toString()
//                } else {
//                    _editProfileMessage.value = "Edit Profile Failed"
//                }
//            } catch (e: Exception) {
//                _editProfileMessage.value = "Failed Connect, Try Again"
//            }
//        }
//    }
}
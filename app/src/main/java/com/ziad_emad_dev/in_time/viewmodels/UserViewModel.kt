package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import kotlinx.coroutines.launch

class UserViewModel(context: Context) : ViewModel() {

    private val context by lazy {
        context
    }

    private val _message = MutableLiveData<String>()
    val message get() = _message

    private val _name = MutableLiveData<String>()
    val name get() = _name

    private val _email = MutableLiveData<String>()
    val email get() = _email

    private val _phone = MutableLiveData<String>()
    val phone get() = _phone

//    fun fetchUser() {
//        viewModelScope.launch {
//            try {
//                val response = InTimeApi.retrofitService.fetchUser(SessionManager(context).fetchAuthToken().toString())
//                if (response.isSuccessful) {
//                    _message.value = response.body()?.success.toString()
//                    _name.value = response.body()?.record?.name
//                    _email.value = response.body()?.record?.email
//                    _phone.value = response.body()?.record?.phone.toString()
//                    _message.value = "Response Success"
//                } else {
//                    _message.value = "Response Failed"
//                }
//            } catch (e: Exception) {
//                _message.value = "Failed Connect, Try Again"
//            }
//        }
//    }

    fun fetchUser() {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.fetchUser("Bearer ${SessionManager(context).fetchAuthToken().toString()}")
                if (response.isSuccessful) {
                    _message.value = response.body()?.success.toString()
                    _name.value = response.body()?.record?.name
                    _email.value = response.body()?.record?.email
                    _phone.value = response.body()?.record?.phone.toString()
                    _message.value = "Response Success"
                } else {
                    _message.value = "Response Failed with status code: ${response.code()}, message: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Failed Connect, Try Again. Exception: ${e.message}"
            }
        }
    }
}
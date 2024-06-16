package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziad_emad_dev.in_time.network.auth.SessionManager

class TaskViewModel(context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _createTaskMessage = MutableLiveData<String>()
    val createTaskMessage get() = _createTaskMessage

//    fun createTask(name: String, disc: String, tag: String, priority: Int, steps: List<String>, imagePath: String?) {
//
//        viewModelScope.launch {
//
//                val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
//                val discPart = RequestBody.create("text/plain".toMediaTypeOrNull(), disc)
//                val tagPart = RequestBody.create("text/plain".toMediaTypeOrNull(), tag)
//                val priorityPart = RequestBody.create("text/plain".toMediaTypeOrNull(), priority.toString())
//
//                val stepParts = steps.mapIndexed { index, step ->
//                    MultipartBody.Part.createFormData("steps[$index][stepDisc]", step)
//                }
//
//                val imageFile = File(imagePath)
//                val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, RequestBody.create("image/*".toMediaTypeOrNull(), imageFile))
//
//            try {
//                val response = InTimeApi.retrofitService.createTask(
//                    "Bearer ${sessionManager.fetchAuthToken()}",
//                    namePart,
//                    discPart,
//                    tagPart,
//                    priorityPart,
//                    stepParts,
//                    imagePart
//                )
//                if (response.isSuccessful) {
//                    _createTaskMessage.value = "Request Succeed"
//                } else {
//                    _createTaskMessage.value = "Request Failed"
//                }
//            } catch (e: Exception) {
//                _createTaskMessage.value = "Failed Connect, Try Again"
//            }
//        }
//    }
}
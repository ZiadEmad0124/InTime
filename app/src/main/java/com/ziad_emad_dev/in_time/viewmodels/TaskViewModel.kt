package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.tasks.Tag
import com.ziad_emad_dev.in_time.network.tasks.Task
import com.ziad_emad_dev.in_time.network.tasks.create_task.CreateTaskResponse
import com.ziad_emad_dev.in_time.network.tasks.delete_task.DeleteTaskResponse
import com.ziad_emad_dev.in_time.network.tasks.update_task.UpdateTaskResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class TaskViewModel(context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _createTaskMessage = MutableLiveData<String>()
    val createTaskMessage get() = _createTaskMessage

    private val _getTasksMessage = MutableLiveData<String>()
    val getTasksMessage get() = _getTasksMessage

    private val _getTasks = MutableLiveData<List<Task>>()
    val getTasks get() = _getTasks

    private val _getTaskMessage = MutableLiveData<String>()
    val getTaskMessage get() = _getTaskMessage

    private val _getTask = MutableLiveData<Task>()
    val getTask get() = _getTask

    private val _deleteTaskMessage = MutableLiveData<String>()
    val deleteTaskMessage get() = _deleteTaskMessage

    private val _updateTaskMessage = MutableLiveData<String>()
    val updateTaskMessage get() = _updateTaskMessage

    fun createTask(name: String, description: String?, priority: String, startAt: String, endAt: String, taskCoverFile: File?, steps: List<String>?, tag: Tag) {

        val myName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val myDescription = description?.toRequestBody("text/plain".toMediaTypeOrNull())
        val myPriority = priority.toRequestBody("text/plain".toMediaTypeOrNull())

        val myStartAt = startAt.toRequestBody("text/plain".toMediaTypeOrNull())
        val myEndAt = endAt.toRequestBody("text/plain".toMediaTypeOrNull())

        val myTaskCoverFile = if (taskCoverFile == null) {
            null
        } else {
            val requestFile = taskCoverFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", taskCoverFile.name, requestFile)
        }

        val stepParts = if (steps.isNullOrEmpty()) {
            null
        } else {
            steps.mapIndexed { index, step ->
                MultipartBody.Part.createFormData("steps[$index][stepDisc]", step)
            }
        }

        val tagName = tag.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val tagColor = tag.color.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.createTask("Bearer ${sessionManager.fetchAuthToken().toString()}",
                        myName, myDescription, myPriority, myStartAt, myEndAt, myTaskCoverFile, stepParts, tagName, tagColor)
                if (response.isSuccessful) {
                    _createTaskMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorCreateTaskResponse = Gson().fromJson(errorResponse, CreateTaskResponse::class.java)
                    _createTaskMessage.value = errorCreateTaskResponse.message.toString()
                }
            } catch (e: Exception) {
                _createTaskMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun getTasks() {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getTasks("Bearer ${sessionManager.fetchAuthToken().toString()}", 1)
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

    fun getTask(taskId: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getTask("Bearer ${sessionManager.fetchAuthToken().toString()}", taskId)
                if (response.isSuccessful) {
                    _getTaskMessage.value = response.body()?.success.toString()
                    _getTask.value = response.body()?.record!!
                } else {
                    _getTaskMessage.value = "Get Task failed"
                }
            } catch (e: Exception) {
                _getTaskMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.deleteTask("Bearer ${sessionManager.fetchAuthToken().toString()}", taskId)
                if (response.isSuccessful) {
                    _deleteTaskMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorDeleteTaskResponse = Gson().fromJson(errorResponse, DeleteTaskResponse::class.java)
                    _deleteTaskMessage.value = errorDeleteTaskResponse.message.toString()
                }
            } catch (e: Exception) {
                _deleteTaskMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun updateTask(id: String, name: String?, description: String?, priority: String,
                   startAt: String, endAt: String, taskCoverFile: File?, steps: List<String>?, tag: Tag) {

        val myName = name?.toRequestBody("text/plain".toMediaTypeOrNull())
        val myDescription = description?.toRequestBody("text/plain".toMediaTypeOrNull())
        val myPriority = priority.toRequestBody("text/plain".toMediaTypeOrNull())

        val myStartAt = startAt.toRequestBody("text/plain".toMediaTypeOrNull())
        val myEndAt = endAt.toRequestBody("text/plain".toMediaTypeOrNull())

        val myTaskCoverFile = if (taskCoverFile == null) {
            null
        } else {
            val requestFile = taskCoverFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", taskCoverFile.name, requestFile)
        }

        val stepParts = if (steps.isNullOrEmpty()) {
            null
        } else {
            steps.mapIndexed { index, step ->
                MultipartBody.Part.createFormData("steps[$index][stepDisc]", step)
            }
        }

        val tagName = tag.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val tagColor = tag.color.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.updateTask(
                    "Bearer ${sessionManager.fetchAuthToken().toString()}", id,
                    myName, myDescription, myPriority, myStartAt, myEndAt, myTaskCoverFile, stepParts)
                if (response.isSuccessful) {
                    _updateTaskMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorCreateTaskResponse = Gson().fromJson(errorResponse, UpdateTaskResponse::class.java)
                    _updateTaskMessage.value = errorCreateTaskResponse.message.toString()
                }
            } catch (e: Exception) {
                _updateTaskMessage.value = "Failed Connect, Try Again"
            }
        }
    }
}
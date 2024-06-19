package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.project.create_project.CreateProjectResponse
import com.ziad_emad_dev.in_time.network.project.get_projects.Project
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProjectViewModel(context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _createProjectMessage = MutableLiveData<String>()
    val createProjectMessage get() = _createProjectMessage

    private val _getProjectsMessage = MutableLiveData<String>()
    val getProjectsMessage get() = _getProjectsMessage

    private val _getProjects = MutableLiveData<List<Project>>()
    val getProjects get() = _getProjects

    fun createProject(name: String, projectCoverFile: File?) {

        val myName = name.toRequestBody("text/plain".toMediaTypeOrNull())

        val myProjectCoverFile = if (projectCoverFile == null) {
            null
        } else {
            val requestFile = projectCoverFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", projectCoverFile.name, requestFile)
        }

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.createProject("Bearer ${sessionManager.fetchAuthToken().toString()}",
                    myName, myProjectCoverFile)
                if (response.isSuccessful) {
                    _createProjectMessage.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorCreateTProjectResponse = Gson().fromJson(errorResponse, CreateProjectResponse::class.java)
                    _createProjectMessage.value = errorCreateTProjectResponse.message.toString()
                }
            } catch (e: Exception) {
                _createProjectMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun getProjects() {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getProjects("Bearer ${sessionManager.fetchAuthToken().toString()}")
                if (response.isSuccessful) {
                    _getProjectsMessage.value = "Get all projects success"
                    _getProjects.value = response.body()
                } else {
                    _getProjectsMessage.value = "Get all projects failed"
                }
            } catch (e: Exception) {
                _getProjectsMessage.value = "Failed Connect, Try Again"
            }
        }
    }
}
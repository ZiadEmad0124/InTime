package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.project.Project
import com.ziad_emad_dev.in_time.network.project.create_project.CreateProjectResponse
import com.ziad_emad_dev.in_time.network.project.project_members.MemberRecord
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

    private val _getProjectMessage = MutableLiveData<String>()
    val getProjectMessage get() = _getProjectMessage

    private val _getProject = MutableLiveData<Project>()
    val getProject get() = _getProject

    private val _editProjectMessage = MutableLiveData<String>()
    val editProjectMessage get() = _editProjectMessage

    private val _removeProjectCoverMessage = MutableLiveData<String>()
    val removeProjectCoverMessage get() = _removeProjectCoverMessage

    private val _shareProjectMessage = MutableLiveData<String>()
    val shareProjectMessage get() = _shareProjectMessage

    private val _shareProjectLink = MutableLiveData<String>()
    val shareProjectLink get() = _shareProjectLink

    private val _getMembersMessage = MutableLiveData<String>()
    val getMembersMessage get() = _getMembersMessage

    private val _getMembers = MutableLiveData<List<MemberRecord>>()
    val getMembers get() = _getMembers

    private val _deleteProjectMessage = MutableLiveData<String>()
    val deleteProjectMessage get() = _deleteProjectMessage

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

    fun getProjects(role: String?) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getProjects("Bearer ${sessionManager.fetchAuthToken().toString()}", role)
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

    fun getProject(projectId: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getProject("Bearer ${sessionManager.fetchAuthToken().toString()}", projectId)
                if (response.isSuccessful) {
                    _getProjectMessage.value = response.body()?.success.toString()
                    _getProject.value = response.body()?.record!!
                } else {
                    _getProjectMessage.value = "Get project failed"
                }
            } catch (e: Exception) {
                _getProjectMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun editProject(projectId: String, name: String?, projectCoverFile: File?) {

        val myName = name?.toRequestBody("text/plain".toMediaTypeOrNull())

        val myProjectCoverFile = if (projectCoverFile == null) {
            null
        } else {
            val requestFile = projectCoverFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", projectCoverFile.name, requestFile)
        }

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.editProject("Bearer ${sessionManager.fetchAuthToken().toString()}",
                    projectId, myName, myProjectCoverFile)
                if (response.isSuccessful) {
                    _editProjectMessage.value = response.body()?.success.toString()
                } else {
                    _editProjectMessage.value = "edit project failed"
                }
            } catch (e: Exception) {
                _editProjectMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun removeCover(projectId: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.removeProjectCover("Bearer ${sessionManager.fetchAuthToken().toString()}", projectId)
                if (response.isSuccessful) {
                    _removeProjectCoverMessage.value = "true"
                } else {
                    _removeProjectCoverMessage.value = "remove cover failed"
                }
            } catch (e: Exception) {
                _removeProjectCoverMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun shareProject(projectId: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.shareProject("Bearer ${sessionManager.fetchAuthToken().toString()}", projectId)
                if (response.isSuccessful) {
                    _shareProjectMessage.value = response.body()?.success.toString()
                    _shareProjectLink.value = response.body()?.link.toString()
                } else {
                    _shareProjectMessage.value = "share project failed"
                }
            } catch (e: Exception) {
                _shareProjectMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun getMembers(projectId: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.getProjectMembers("Bearer ${sessionManager.fetchAuthToken().toString()}", projectId)
                if (response.isSuccessful) {
                    _getMembersMessage.value = response.body()?.success.toString()
                    _getMembers.value = response.body()?.record!!
                } else {
                    _getMembersMessage.value = "Get members failed"
                }
            } catch (e: Exception) {
                _getMembersMessage.value = "Failed Connect, Try Again"
            }
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.deleteProject("Bearer ${sessionManager.fetchAuthToken().toString()}", projectId)
                if (response.isSuccessful) {
                    _deleteProjectMessage.value = response.body()?.success.toString()
                } else {
                    _deleteProjectMessage.value = "delete project failed"
                }
            } catch (e: Exception) {
                _deleteProjectMessage.value = "Failed Connect, Try Again"
            }
        }
    }
}
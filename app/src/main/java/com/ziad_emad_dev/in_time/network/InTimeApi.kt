package com.ziad_emad_dev.in_time.network

import com.ziad_emad_dev.in_time.network.auth.activation.ActivationRequest
import com.ziad_emad_dev.in_time.network.auth.activation.ActivationResponse
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailRequest
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailResponse
import com.ziad_emad_dev.in_time.network.auth.new_password.ResetPasswordRequest
import com.ziad_emad_dev.in_time.network.auth.new_password.ResetPasswordResponse
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenRequest
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenResponse
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeRequest
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeResponse
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInRequest
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInResponse
import com.ziad_emad_dev.in_time.network.auth.sign_out.SignOutRequest
import com.ziad_emad_dev.in_time.network.auth.sign_out.SignOutResponse
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpRequest
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpResponse
import com.ziad_emad_dev.in_time.network.profile.change_password.ChangePasswordRequest
import com.ziad_emad_dev.in_time.network.profile.change_password.ChangePasswordResponse
import com.ziad_emad_dev.in_time.network.profile.delete_account.DeleteAccountResponse
import com.ziad_emad_dev.in_time.network.profile.edit_profile.EditProfileResponse
import com.ziad_emad_dev.in_time.network.profile.rank.RankResponse
import com.ziad_emad_dev.in_time.network.profile.remove_avatar.RemoveAvatarResponse
import com.ziad_emad_dev.in_time.network.profile.user.UserResponse
import com.ziad_emad_dev.in_time.network.project.delete_project.DeleteProjectResponse
import com.ziad_emad_dev.in_time.network.project.delete_project_cover.DeleteProjectCoverResponse
import com.ziad_emad_dev.in_time.network.project.get_project.GetProjectResponse
import com.ziad_emad_dev.in_time.network.project.get_projects.GetProjectsResponse
import com.ziad_emad_dev.in_time.network.project.project_members.GetProjectMembersResponse
import com.ziad_emad_dev.in_time.network.project.remove_member.RemoveMemberResponse
import com.ziad_emad_dev.in_time.network.project.share_project.ShareProjectResponse
import com.ziad_emad_dev.in_time.network.project.tasks.create_task.CreateProjectTaskResponse
import com.ziad_emad_dev.in_time.network.project.tasks.get_task.GetProjectTaskResponse
import com.ziad_emad_dev.in_time.network.project.tasks.remove_task.DeleteProjectTaskResponse
import com.ziad_emad_dev.in_time.network.tasks.complete_task.CompleteTaskResponse
import com.ziad_emad_dev.in_time.network.tasks.create_task.CreateTaskResponse
import com.ziad_emad_dev.in_time.network.tasks.delete_cover.DeleteTaskCover
import com.ziad_emad_dev.in_time.network.tasks.delete_task.DeleteTaskResponse
import com.ziad_emad_dev.in_time.network.tasks.get_task.GetTaskResponse
import com.ziad_emad_dev.in_time.network.tasks.get_tasks.GetTasksResponse
import com.ziad_emad_dev.in_time.network.tasks.update_task.UpdateTaskResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://intime-9hga.onrender.com/api/v1/"

//  Auth Endpoints
private const val SIGNIN_URL = "auth/login"
private const val SIGNUP_URL = "auth/signup"
private const val ACTIVATION_CODE_URL = "auth/activation/{code}"
private const val RESEND_ACTIVATION_CODE_URL = "auth/resendactivationcode"
private const val CHECK_EMAIL_URL = "auth/forgetpassword"
private const val FORGET_PASSWORD_URL = "auth/forgetpassword/changepassword/{code}"
private const val REFRESH_TOKEN_URL = "auth/refreshToken"
private const val SIGNOUT_URL = "auth/signOut"

//  User Endpoints
private const val USER_URL = "user/"
private const val DELETE_ACCOUNT_URL = "user/deleteUser"
private const val EDIT_PROFILE_URL = "user/editProfile"
private const val REMOVE_AVATAR_URL = "user/deleteProfilePhoto"
private const val CHANGE_PASSWORD_URL = "user/changePassword"
private const val USER_RANK_URL = "user/getUsersRank"

//  Tasks Endpoints
private const val CREATE_TASK_URL = "user/tasks/addUserTask"
private const val GET_TASKS_URL = "user/tasks/"
private const val GET_ALL_TAG_URL = "user/tasks/?page=1&size=0&sortingType=1"
private const val GET_TASK_URL = "user/tasks/{taskId}"
private const val DELETE_TASK_URL = "user/tasks/deleteById/{taskId}"
private const val UPDATE_TASK_URL = "user/tasks/updateById/{taskId}"
private const val UPDATE_STEPS_URL = "user/tasks/updateById/{taskId}"
private const val COMPLETE_TASK_URL = "user/tasks/completeTask/{taskId}"
private const val SEARCH_TASK_URL = "user/tasks/searchTasks/{search}"
private const val REMOVE_TASK_COVER_URL = "user/tasks/removeTaskImage/{taskId}"

//  Projects Endpoints
private const val CREATE_PROJECT_URL = "user/projects/createProject"
private const val GET_PROJECTS_URL = "user/projects/myProjects"
private const val GET_PROJECT_URL = "user/projects/{projectId}"
private const val DELETE_PROJECT_URL = "user/projects/removeProject/{projectId}"
private const val EDIT_PROJECT_URL = "user/projects/editProject/{projectId}"
private const val REMOVE_PROJECT_COVER_URL = "user/projects/removeProjectImage/{projectId}"
private const val GET_PROJECT_MEMBERS_URL = "user/projects/projectMembers/{projectId}"
private const val JOIN_PROJECT_URL = "user/projects/joinProject/{link}"
private const val REMOVE_PROJECT_MEMBER_URL = "user/projects/removeMember/{projectId}/{memberId}"
private const val SHARE_PROJECT_URL = "user/projects/generateInviteLink/{projectId}"
private const val CREATE_PROJECT_TASK_URL = "user/projects/assignTask/{projectId}/{memberId}"
private const val GET_PROJECT_TASKS_URL = "user/projects/getProjectTasks/{projectId}"
private const val EDIT_PROJECT_TASK_ADMIN_URL = "user/projects/editProjectTask/{projectId}/{taskId}"
private const val REMOVE_PROJECT_TASKS_URL = "user/projects/deleteProjectTask/{projectId}/{taskId}"


private val okHttpClient = OkHttpClient.Builder()
    .readTimeout(300, TimeUnit.SECONDS)
    .connectTimeout(300, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface InTimeApiServices {

//  Auth

    @POST(SIGNIN_URL)
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST(SIGNUP_URL)
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST(ACTIVATION_CODE_URL)
    suspend fun activateAccount(@Path("code") code: String, @Body request: ActivationRequest): Response<ActivationResponse>

    @POST(RESEND_ACTIVATION_CODE_URL)
    suspend fun resendActivationCode(@Body request: ResendActivationCodeRequest): Response<ResendActivationCodeResponse>

    @POST(CHECK_EMAIL_URL)
    suspend fun checkEmail(@Body request: CheckEmailRequest): Response<CheckEmailResponse>

    @POST(FORGET_PASSWORD_URL)
    suspend fun resetPassword(@Path("code") code: String, @Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @POST(REFRESH_TOKEN_URL)
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST(SIGNOUT_URL)
    suspend fun signOut(@Body request: SignOutRequest): Response<SignOutResponse>

//  User

    @GET(USER_URL)
    suspend fun fetchUser(@Header("Authorization") token: String): Response<UserResponse>

    @DELETE(DELETE_ACCOUNT_URL)
    suspend fun deleteAccount(@Header("Authorization") token: String): Response<DeleteAccountResponse>

    @Multipart
    @POST(EDIT_PROFILE_URL)
    suspend fun editProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("title") title: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("about") about: RequestBody,
        @Part avatar: MultipartBody.Part?
    ): Response<EditProfileResponse>

    @DELETE(REMOVE_AVATAR_URL)
    suspend fun removeAvatar(@Header("Authorization") token: String): Response<RemoveAvatarResponse>

    @POST(CHANGE_PASSWORD_URL)
    suspend fun changePassword(@Header("Authorization") token: String, @Body request: ChangePasswordRequest): Response<ChangePasswordResponse>

    @GET(USER_RANK_URL)
    suspend fun fetchUserRank(@Header("Authorization") token: String): Response<RankResponse>

//  Tasks

    @Multipart
    @POST(CREATE_TASK_URL)
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("disc") disc: RequestBody?,
        @Part("priority") priority: RequestBody,
        @Part("startAt") startAt: RequestBody,
        @Part("endAt") endAt: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part steps: List<MultipartBody.Part>?,
        @Part("tag[name]") tagName: RequestBody,
        @Part("tag[color]") tagColor: RequestBody
    ): Response<CreateTaskResponse>

    @GET(GET_TASKS_URL)
    suspend fun getTasks(
        @Header("Authorization") token: String,
        @Query("sortingType") sortingType: Int,
        @Query("projectTask") projectTask: Boolean,
        ): Response<GetTasksResponse>

    @GET(GET_ALL_TAG_URL)
    suspend fun getAllTag(@Header("Authorization") token: String): Response<GetTasksResponse>

    @GET(GET_TASK_URL)
    suspend fun getTask(@Header("Authorization") token: String, @Path("taskId") taskId: String): Response<GetTaskResponse>

    @POST(DELETE_TASK_URL)
    suspend fun deleteTask(@Header("Authorization") token: String, @Path("taskId") taskId: String): Response<DeleteTaskResponse>

    @POST(COMPLETE_TASK_URL)
    suspend fun completeTask(@Header("Authorization") token: String, @Path("taskId") search: String): Response<CompleteTaskResponse>

    @GET(SEARCH_TASK_URL)
    suspend fun searchTasks(@Header("Authorization") token: String, @Path("search") search: String): Response<GetTasksResponse>

    @DELETE(REMOVE_TASK_COVER_URL)
    suspend fun removeTaskCover(@Header("Authorization") token: String, @Path("taskId") taskId: String): Response<DeleteTaskCover>

    @Multipart
    @POST(UPDATE_TASK_URL)
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Path("taskId") taskId: String,
        @Part("name") name: RequestBody?,
        @Part("disc") disc: RequestBody?,
        @Part("priority") priority: RequestBody,
        @Part("startAt") startAt: RequestBody,
        @Part("endAt") endAt: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part steps: List<MultipartBody.Part>?,
        @Part("tag[name]") tagName: RequestBody,
        @Part("tag[color]") tagColor: RequestBody
    ): Response<UpdateTaskResponse>

    @Multipart
    @POST(UPDATE_STEPS_URL)
    suspend fun updateSteps(
        @Header("Authorization") token: String,
        @Path("taskId") taskId: String,
        @Part stepsName: List<MultipartBody.Part>,
        @Part stepsCompleted: List<MultipartBody.Part>,
        ): Response<UpdateTaskResponse>

//    Projects

    @Multipart
    @POST(CREATE_PROJECT_URL)
    suspend fun createProject(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<CreateTaskResponse>

    @GET(GET_PROJECTS_URL)
    suspend fun getProjects(@Header("Authorization") token: String, @Query("role") role: String?): Response<GetProjectsResponse>

    @GET(GET_PROJECT_URL)
    suspend fun getProject(@Header("Authorization") token: String, @Path("projectId") projectId: String): Response<GetProjectResponse>

    @Multipart
    @POST(EDIT_PROJECT_URL)
    suspend fun editProject(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: String,
        @Part("name") name: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<GetProjectResponse>

    @GET(SHARE_PROJECT_URL)
    suspend fun shareProject(@Header("Authorization") token: String, @Path("projectId") projectId: String): Response<ShareProjectResponse>

    @GET(JOIN_PROJECT_URL)
    suspend fun joinProject(
        @Header("Authorization") token: String,
        @Path("link") link: String,
    ): Response<GetProjectResponse>

    @DELETE(REMOVE_PROJECT_COVER_URL)
    suspend fun removeProjectCover(@Header("Authorization") token: String, @Path("projectId") projectId: String): Response<DeleteProjectCoverResponse>

    @GET(GET_PROJECT_MEMBERS_URL)
    suspend fun getProjectMembers(@Header("Authorization") token: String, @Path("projectId") projectId: String): Response<GetProjectMembersResponse>

    @DELETE(REMOVE_PROJECT_MEMBER_URL)
    suspend fun removeProjectMember(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: String,
        @Path("memberId") memberId: String): Response<RemoveMemberResponse>

    @DELETE(DELETE_PROJECT_URL)
    suspend fun deleteProject(@Header("Authorization") token: String, @Path("projectId") projectId: String): Response<DeleteProjectResponse>

    @Multipart
    @POST(CREATE_PROJECT_TASK_URL)
    suspend fun createProjectTask(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: String,
        @Path("memberId") memberId: String,
        @Part("name") name: RequestBody,
        @Part("disc") disc: RequestBody?,
        @Part("startAt") startAt: RequestBody,
        @Part("endAt") endAt: RequestBody
    ): Response<CreateProjectTaskResponse>

    @GET(GET_PROJECT_TASKS_URL)
    suspend fun getProjectTasks(@Header("Authorization") token: String, @Path("projectId") projectId: String): Response<GetProjectTaskResponse>

    @Multipart
    @POST(EDIT_PROJECT_TASK_ADMIN_URL)
    suspend fun editProjectTaskAdmin(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: String,
        @Path("taskId") taskId: String,
        @Part("name") name: RequestBody,
        @Part("disc") disc: RequestBody?,
        @Part("startAt") startAt: RequestBody,
        @Part("endAt") endAt: RequestBody
    ): Response<CreateProjectTaskResponse>

    @DELETE(REMOVE_PROJECT_TASKS_URL)
    suspend fun deleteProjectTask(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: String,
        @Path("taskId") taskId: String
    ): Response<DeleteProjectTaskResponse>
}

object InTimeApi {
    val retrofitService: InTimeApiServices by lazy {
        retrofit.create(InTimeApiServices::class.java)
    }
}
package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import ru.vsu.cs.tp.richfamily.api.model.group.DeleteUserRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.Group
import ru.vsu.cs.tp.richfamily.api.model.group.GroupRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUser
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUserRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.Leader

interface GroupApi {

    @Headers("Content-type: application/json")
    @GET("api/v1/users/groups")
    suspend fun getUsersGroup(@Header("Authorization") token: String) : Response<List<Group>>

    @Headers("Content-type: application/json")
    @GET("api/v1/groups/{id}/users/")
    suspend fun getAllUsersFromGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        ) : Response<List<GroupUser>>

    @Headers("Content-type: application/json")
    @GET("api/v1/groups/{id}/is_leader/")
    suspend fun isLeader(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ) : Response<Leader>

    @Headers("Content-type: application/json")
    @POST("api/v1/groups/")
    suspend fun addGroup(
        @Header("Authorization") token: String,
        @Body groupRequestBody: GroupRequestBody
    ) : Response<Group>

    @Headers("Content-type: application/json")
    @POST("api/v1/groups/{id}/add_user/")
    suspend fun addUserInGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body groupUserRequestBody: GroupUserRequestBody
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("api/v1/groups/{id}/remove_user/")
    suspend fun deleteUserFromGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body deleteUserRequestBody: DeleteUserRequestBody
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("api/v1/groups/{id}/exit_from_group/")
    suspend fun leaveFromGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ) : Response<ResponseBody>

    companion object {
        fun getGroupApi() : GroupApi? {
            return ClientApi.client?.create(GroupApi::class.java)
        }
    }
}
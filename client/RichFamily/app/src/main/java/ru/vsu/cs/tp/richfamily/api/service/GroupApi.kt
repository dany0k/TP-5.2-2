package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vsu.cs.tp.richfamily.api.model.group.DeleteUserRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.Group
import ru.vsu.cs.tp.richfamily.api.model.group.GroupRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUser
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUserRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.Leader
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation

interface GroupApi {

    @Headers("Content-type: application/json")
    @GET("users/groups/")
    suspend fun getUsersGroup(@Header("Authorization") token: String) : Response<List<Group>>

    @Headers("Content-type: application/json")
    @GET("groups/{id}/users/")
    suspend fun getAllUsersFromGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        ) : Response<List<GroupUser>>

    @Headers("Content-type: application/json")
    @GET("groups/{id}/is_leader/")
    suspend fun isLeader(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ) : Response<Leader>

    @Headers("Content-type: application/json")
    @POST("groups/")
    suspend fun addGroup(
        @Header("Authorization") token: String,
        @Body groupRequestBody: GroupRequestBody
    ) : Response<Group>

    @Headers("Content-type: application/json")
    @POST("groups/{id}/add_user/")
    suspend fun addUserInGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body groupUserRequestBody: GroupUserRequestBody
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("groups/{id}/remove_user/")
    suspend fun deleteUserFromGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body deleteUserRequestBody: DeleteUserRequestBody
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("groups/{id}/exit_from_group/")
    suspend fun leaveFromGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @DELETE("groups/{id}/")
    suspend fun deleteGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ) : Response<ResponseBody>


    @Headers("Content-type: application/json")
    @GET("users/{user_id}/operations/")
    suspend fun getUsersOperations(
        @Header("Authorization") token: String,
        @Path("user_id") userId: Int,
        @Query("group") groupId: Int,
    ) : Response<List<Operation>>

    companion object {
        fun getGroupApi() : GroupApi? {
            return ClientApi.client?.create(GroupApi::class.java)
        }
    }
}
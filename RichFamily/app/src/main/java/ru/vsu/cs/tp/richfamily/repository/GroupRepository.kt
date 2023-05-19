package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.group.Group
import ru.vsu.cs.tp.richfamily.api.model.group.GroupRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUser
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUserRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.DeleteUserRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.Leader
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation
import ru.vsu.cs.tp.richfamily.api.service.GroupApi

class GroupRepository(
    private val groupApi: GroupApi,
    private val token: String
) {
    suspend fun getUsersGroup(): Response<List<Group>> {
        return groupApi.getUsersGroup(token = token)
    }

    suspend fun addGroup(groupRequestBody: GroupRequestBody): Response<Group> {
        return groupApi.addGroup(
            token = token,
            groupRequestBody = groupRequestBody
        )
    }

    suspend fun getAllUsersFromGroup(id: Int): Response<List<GroupUser>> {
        return groupApi.getAllUsersFromGroup(token = token, id = id)
    }

    suspend fun addUserInGroup(
        groupId: Int,
        groupUserRequestBody: GroupUserRequestBody
    ): Response<ResponseBody> {
        return groupApi.addUserInGroup(
            token = token,
            id = groupId,
            groupUserRequestBody = groupUserRequestBody
        )
    }

    suspend fun deleteUserFromGroup(
        groupId: Int,
        deleteUserRequestBody: DeleteUserRequestBody
    ): Response<ResponseBody> {
        return groupApi.deleteUserFromGroup(
            token = token,
            id = groupId,
            deleteUserRequestBody = deleteUserRequestBody)
    }

    suspend fun leaveFromGroup(
        groupId: Int,
    ): Response<ResponseBody> {
        return groupApi.leaveFromGroup(
            token = token,
            id = groupId
        )
    }

    suspend fun deleteGroup(
        groupId: Int,
    ): Response<ResponseBody> {
        return groupApi.deleteGroup(
            token = token,
            id = groupId
        )
    }

    suspend fun getUsersOperations(userId: Int, groupId: Int): Response<List<Operation>> {
        return groupApi.getUsersOperations(token = token, userId = userId, groupId = groupId)
    }

    suspend fun isLeader(groupId: Int): Response<Leader> {
        return groupApi.isLeader(token = token, id = groupId)
    }
}
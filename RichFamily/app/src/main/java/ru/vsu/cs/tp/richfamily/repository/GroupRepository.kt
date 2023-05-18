package ru.vsu.cs.tp.richfamily.repository

import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.group.Group
import ru.vsu.cs.tp.richfamily.api.service.GroupApi

class GroupRepository(
    private val groupApi: GroupApi,
    private val token: String
) {
    suspend fun getUsersGroup(): Response<List<Group>> {
        return groupApi.getUsersGroup(token = token)
    }
}
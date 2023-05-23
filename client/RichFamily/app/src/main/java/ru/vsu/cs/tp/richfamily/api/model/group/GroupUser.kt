package ru.vsu.cs.tp.richfamily.api.model.group

data class GroupUser (
    val id: Int,
    val first_name: String,
    val last_name: String,
    val is_leader: Boolean
)
package ru.vsu.cs.tp.richfamily.api.model.auth

data class UserRequestBody (
    val id: Int,
    val first_name: String,
    val last_name: String,
)
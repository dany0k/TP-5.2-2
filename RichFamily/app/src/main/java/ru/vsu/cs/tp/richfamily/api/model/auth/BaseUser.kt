package ru.vsu.cs.tp.richfamily.api.model.auth

data class BaseUser(
    val email: String,
    val username: String,
    val id: Int,
)
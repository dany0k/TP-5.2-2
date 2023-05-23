package ru.vsu.cs.tp.richfamily.api.model.auth

data class BaseRegistrationRequest (
    val username: String,
    val email: String,
    val password: String
)
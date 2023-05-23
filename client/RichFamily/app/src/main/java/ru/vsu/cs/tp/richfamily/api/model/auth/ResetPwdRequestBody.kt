package ru.vsu.cs.tp.richfamily.api.model.auth

data class ResetPwdRequestBody(
    val email: String,
    val secret_word: String,
    val new_password: String
)
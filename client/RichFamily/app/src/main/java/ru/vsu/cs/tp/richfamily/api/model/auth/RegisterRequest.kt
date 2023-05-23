package ru.vsu.cs.tp.richfamily.api.model.auth

data class RegisterRequest(
    val user_id: Int,
    val first_name: String,
    val last_name: String,
    val secret_word: String
)
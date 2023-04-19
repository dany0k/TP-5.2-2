package ru.vsu.cs.tp.richfamily.model

//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize
//
//@Parcelize
//data class User(
//    val userId: Int,
//    val name: String,
//    val surname: String,
//    val email: String,
//    val password: String,
//    val secretWord: String,
//    val token: String
//) : Parcelable

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val token: String
)


package ru.vsu.cs.tp.richfamily.api.model.onboarding

import com.google.gson.annotations.SerializedName

data class Onboarding(
    val title: String,
    val description: String,
    @SerializedName("onboard_type")
    val type: String
)
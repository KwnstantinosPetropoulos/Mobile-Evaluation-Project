package com.example.mobile_evaluation_project_2025.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String
)

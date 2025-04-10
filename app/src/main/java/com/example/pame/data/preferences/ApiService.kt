package com.example.pame.data.preferences

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import com.example.mobile_evaluation_project_2025.network.LoginRequest // Εισαγωγή LoginRequest από το σωστό πακέτο
import com.example.mobile_evaluation_project_2025.model.Book // Εισαγωγή Book
import com.example.mobile_evaluation_project_2025.network.LoginResponse

interface ApiService {


    @POST("Login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>



    @GET("Books")
    fun getAvailableBooks(@Header("Authorization") authHeader: String): Call<List<Book>>

}

package com.example.mobile_evaluation_project_2025.repository

import android.content.Context
import android.util.Log
import com.example.mobile_evaluation_project_2025.model.Book
import com.example.pame.data.preferences.ApiService
import com.example.pame.data.preferences.RetrofitClientBooks
import com.example.pame.data.preferences.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookRepository {
    fun getBooks(context: Context, callback: (Result<List<Book>>) -> Unit) {
        val token = UserPreferences.getAccessToken(context)
        if (token.isNullOrEmpty()) {
            callback(Result.failure(Exception("Token is null or empty")))
            return
        }

        val apiService = RetrofitClientBooks.getRetrofitInstance().create(ApiService::class.java)
        val call = apiService.getAvailableBooks("Bearer $token")

        Log.e("API_CALL", "Calling API with token: $token")

        call.enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    Log.e(
                        "API_CALL",
                        "Success: ${response.body()?.size} books received"
                    )
                    callback(Result.success(response.body()!!))
                } else {
                    Log.e(
                        "API_CALL",
                        "Error: ${response.code()} - ${response.message()}"
                    )
                    callback(Result.failure(Exception("Unable to load books")))
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.e(
                    "API_CALL",
                    "Network error: ${t.message}"
                )
                callback(Result.failure(t))
            }
        })
    }
}
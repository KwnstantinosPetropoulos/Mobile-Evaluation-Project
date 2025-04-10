package com.example.pame.data.preferences

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientLogin {
    private const val BASE_URL_LOGIN = "https://3nt-demo-backend.azurewebsites.net/Access/"

    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_LOGIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    val apiService: ApiService = getRetrofitInstance().create(ApiService::class.java)
}

object RetrofitClientBooks {
    private const val BASE_URL_BOOKS = "https://3nt-demo-backend.azurewebsites.net/Access/"

    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_BOOKS)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    val apiService: ApiService = getRetrofitInstance().create(ApiService::class.java)
}

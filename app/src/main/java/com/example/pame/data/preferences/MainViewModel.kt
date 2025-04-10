package com.example.mobile_evaluation_project_2025

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobile_evaluation_project_2025.repository.BookRepository
import com.example.mobile_evaluation_project_2025.model.Book
import com.example.pame.data.preferences.RetrofitClientBooks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun getBooks(token: String) {
        val authHeader = "Bearer $token"
        _isLoading.value = true

        RetrofitClientBooks.apiService.getAvailableBooks(authHeader)
            .enqueue(object : Callback<List<Book>> {
                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    if (response.isSuccessful) {
                        _books.value = response.body() ?: emptyList()
                    } else {
                        setErrorMessage("Δεν ήταν δυνατή η φόρτωση των βιβλίων.")
                    }
                    _isLoading.value = false
                }

                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    setErrorMessage("Σφάλμα στο δίκτυο: ${t.message}")
                    _isLoading.value = false
                }
            })
    }
}
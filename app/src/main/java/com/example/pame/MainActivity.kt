package com.example.pame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_evaluation_project_2025.MainViewModel
import com.example.mobile_evaluation_project_2025.ui.LoginScreen
import com.example.mobile_evaluation_project_2025.ui.MainPageScreen
import com.example.mobile_evaluation_project_2025.ui.MainViewModelFactory
import com.example.mobile_evaluation_project_2025.repository.BookRepository
import com.example.pame.ui.theme.PAMETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = BookRepository()

        val factory = MainViewModelFactory(repository)

        val viewModel: MainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        setContent {
            PAMETheme {
                Surface(modifier = androidx.compose.ui.Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable("main_page_screen") {
                            MainPageScreen(navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}

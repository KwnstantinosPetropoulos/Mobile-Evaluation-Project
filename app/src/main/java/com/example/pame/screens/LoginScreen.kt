package com.example.mobile_evaluation_project_2025.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobile_evaluation_project_2025.network.LoginRequest
import com.example.mobile_evaluation_project_2025.network.LoginResponse
import com.example.pame.data.preferences.RetrofitClientLogin
import com.example.pame.ui.theme.Background40
import com.example.pame.ui.theme.Green40
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material.icons.filled.Error
import com.example.pame.data.preferences.ApiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    var userID by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var isGreek by remember { mutableStateOf(true) }
    var showErrorPopup by remember { mutableStateOf(false) }

    var showUserIDInfoDialog by remember { mutableStateOf(false) }
    var showPasswordInfoDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isGreek) "Σύνδεση" else "Sign In",
                        fontSize = 25.sp,
                        color = Color.White
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background40)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isGreek) "Όνομα Χρήστη" else "UserID",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    IconButton(onClick = { showUserIDInfoDialog = true }) { // Εμφάνιση του dialog για UserID
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = Green40)
                    }
                }
                Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                    OutlinedTextField(
                        value = userID,
                        onValueChange = {
                            userID = it
                            showError = false
                        },
                        isError = showError && !isValidUserID(userID),
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        trailingIcon = {
                            if (showError && !isValidUserID(userID)) {
                                Icon(
                                    imageVector = Icons.Filled.Error,
                                    contentDescription = "Error",
                                    tint = Color.Red
                                )
                            }
                        }
                    )

                    Divider(
                        color = if (showError && !isValidUserID(userID)) Color.Red else Green40,
                        thickness = 2.dp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isGreek) "Κωδικός" else "Password",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    IconButton(onClick = { showPasswordInfoDialog = true }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = Green40)
                    }
                }
                Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                showError = false
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            isError = showError && !isValidPassword(password),
                            textStyle = TextStyle(color = Color.White),
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .background(Color.Transparent),
                            trailingIcon = {
                                if (showError && !isValidPassword(password)) {
                                    Icon(
                                        imageVector = Icons.Filled.Error,
                                        contentDescription = "Error",
                                        tint = Color.Red
                                    )
                                }
                            }
                        )
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(
                                text = if (showPassword) "Hide" else "Show",
                                color = Green40,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Divider(
                        color = if (showError && !isValidPassword(password)) Color.Red else Green40,
                        thickness = 2.dp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (showError) {
                    AlertDialog(
                        onDismissRequest = { showError = false },
                        title = {
                            Text(
                                text = if (isGreek) "Λάθος Διαπιστευτήρια" else "Wrong Credentials",
                                fontWeight = FontWeight.Bold, // Έντονη γραμματοσειρά
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        },
                        text = {
                            Text(
                                text = if (isGreek) "Έχετε εισάγει λανθασμένα διαπιστευτήρια." else "You have entered incorrect credentials.",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        },
                        confirmButton = {
                            Column {
                                // Πράσινη γραμμή
                                Divider(
                                    color = Green40,
                                    thickness = 2.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                )
                                // Κουμπί "Πίσω" ή "Back"
                                TextButton(
                                    onClick = { showError = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                ) {
                                    Text(
                                        text = if (isGreek) "Πίσω" else "Back",
                                        color = Green40,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        },
                        containerColor = Background40,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(315.dp)
                            .wrapContentHeight()
                            .padding(16.dp)
                    )
                }

            }

            Button(
                onClick = {
                    if (userID.isEmpty() || password.isEmpty() || !isValidUserID(userID) || !isValidPassword(password)) {
                        showError = true
                        return@Button
                    }
                    loginUser(userID, password) { result ->
                        result.onSuccess { loginResponse ->
                            Toast.makeText(context, "Επιτυχής Σύνδεση", Toast.LENGTH_SHORT).show()
                            navController.navigate("main_page_screen")
                        }.onFailure {
                            showError = true
                            Toast.makeText(context, "Αποτυχία Σύνδεσης", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
                    .border(2.dp, Green40, RoundedCornerShape(50))
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Green40)
            ) {
                Text(text = if (isGreek) "Σύνδεση" else "Login", color = Green40)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { isGreek = true },
                    modifier = Modifier
                        .width(140.dp)
                        .height(50.dp)
                        .border(2.dp, Green40, RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Green40
                    )
                ) {
                    Text(text = "GR", fontSize = 16.sp)
                }
                Button(
                    onClick = { isGreek = false },
                    modifier = Modifier
                        .width(140.dp)
                        .height(50.dp)
                        .border(2.dp, Green40, RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Green40
                    )
                ) {
                    Text(text = "EN", fontSize = 16.sp)
                }
            }
        }
    }

    if (showUserIDInfoDialog) {
        AlertDialog(
            onDismissRequest = { showUserIDInfoDialog = false },
            title = {
                Text(
                    text = if (isGreek) "Oνομα Xρήστη" else "UserID",
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = if (isGreek) "Πρέπει να ξεκινά με 2 κεφαλαία γράμματα και μετά 4 αριθμοί" else "It must start with 2 capital letters and then 4 numbers",
                    color = Color.White
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showUserIDInfoDialog = false },
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Text(
                        text = if (isGreek) "Κλείσιμο" else "Close",
                        color = Green40
                    )
                }
            },
            containerColor = Background40,
            shape = RoundedCornerShape(8.dp)
        )
    }

    // Εμφάνιση Dialog για το Password
    if (showPasswordInfoDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordInfoDialog = false },
            title = {
                Text(
                    text = if (isGreek) "Κωδικός" else "Password",
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = if (isGreek) "Τουλάχιστον 8 χαρακτήρες (2 κεφαλαία, 3 πεζά, 1 ειδικός χαρακτήρας, 2 αριθμοί)" else "At least 8 characters (2 upperscale, 3 lowercase, 1 special character, 2 numbers)",
                    color = Color.White
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showPasswordInfoDialog = false },
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Text(
                        text = if (isGreek) "Κλείσιμο" else "Close",
                        color = Green40
                    )
                }
            },
            containerColor = Background40,
            shape = RoundedCornerShape(8.dp)
        )
    }
}

fun isValidUserID(userID: String): Boolean {
    return userID.length >= 5
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8
}

fun loginUser(userID: String, password: String, callback: (Result<LoginResponse>) -> Unit) {
    val apiService = RetrofitClientLogin.getRetrofitInstance().create(ApiService::class.java)
    val call = apiService.login(LoginRequest(userID, password))

    call.enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful && response.body() != null) {
                Log.d("LoginSuccess", "Response: ${response.body()}")
                callback(Result.success(response.body()!!))
            } else {
                Log.e("LoginError", "Error code: ${response.code()} - ${response.message()}")
                callback(Result.failure(Exception("Login failed: ${response.message()}")))
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Log.e("LoginError", "Failure: ${t.localizedMessage}")
            callback(Result.failure(t))
        }

    })
}

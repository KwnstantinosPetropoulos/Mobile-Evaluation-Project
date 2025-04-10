package com.example.mobile_evaluation_project_2025.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mobile_evaluation_project_2025.MainViewModel
import com.example.mobile_evaluation_project_2025.model.Book
import com.example.pame.ui.theme.Background40
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.ui.res.painterResource
import com.example.mobile_evaluation_project_2025.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageScreen(navController: NavController, viewModel: MainViewModel) {
    val token = "Bearer T1amGT21.Idup.298885bf38e99053dca3434eb59c6aa"
    val books by viewModel.books.observeAsState(initial = emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState(initial = "")
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val isGreek = remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isGreek.value) "Magazines" else "Magazines",
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

        LaunchedEffect(Unit) {
            if (!token.isNullOrEmpty()) {
                viewModel.getBooks(token)
            } else {
                viewModel.setErrorMessage("Δεν βρέθηκε το token. Παρακαλώ συνδεθείτε ξανά.")
            }
        }

        var selectedTab by remember { mutableStateOf(0) }

        Scaffold(
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Book, contentDescription = "Magazines", tint = if (selectedTab == 0) Color.Green else Color.Gray) },
                            label = { Text("Magazine") },
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )

                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.QrCodeScanner, contentDescription = "Scan", tint = if (selectedTab == 1) Color.Green else Color.Gray) },
                            label = { Text("Scan") },
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )


                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.Green, shape = CircleShape)
                                .padding(8.dp)
                                .clickable {
                                    Log.d("PlayButton", "Play button clicked")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile", tint = if (selectedTab == 2) Color.Green else Color.Gray) },
                            label = { Text("Profile") },
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 }
                        )

                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = if (selectedTab == 3) Color.Green else Color.Gray) },
                            label = { Text("Settings") },
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 }
                        )
                    }
                }
            }
        )
        { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                when (selectedTab) {
                    0 -> MagazineScreen(books, isLoading, errorMessage)
                    1 -> Text(text = "", style = MaterialTheme.typography.headlineLarge.copy(color = Color.White))
                    2 -> Text(text = "", style = MaterialTheme.typography.headlineLarge.copy(color = Color.White))
                    3 -> Text(text = "", style = MaterialTheme.typography.headlineLarge.copy(color = Color.White))
                }
            }
        }
    }
}

@Composable
fun MagazineScreen(books: List<Book>, isLoading: Boolean, errorMessage: String) {
    val sortedBooks = books.sortedByDescending { it.date_released }

    Box(modifier = Modifier.fillMaxSize().background(Background40)) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        } else if (sortedBooks.isEmpty()) {
            Text(text = "Δεν βρέθηκαν περιοδικά.", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(sortedBooks) { index, book ->
                    MagazineItem(book)
                }
            }
        }
    }
}

@Composable
fun MagazineItem(book: Book) {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val date = inputFormat.parse(book.date_released)
    val formattedDate = date?.let { outputFormat.format(it) } ?: book.date_released

    var isDownloaded by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = formattedDate ?: "Unknown Date",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(250.dp)
                .background(Color.Gray.copy(alpha = if (!isDownloaded) 0.6f else 1f))
                .align(Alignment.CenterHorizontally)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        isDownloaded = true
                    })
                }
        ) {
            val imagePainter = rememberAsyncImagePainter(
                model = book.img_url.ifEmpty { "dummy_url" },
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.error_image)
            )

            Image(
                painter = imagePainter,
                contentDescription = "Magazine Cover",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (!isDownloaded) {
                if (isHovered) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.Green, shape = CircleShape)
                            .clickable {
                                isDownloaded = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "Download",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            if (isDownloaded) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Green, shape = CircleShape)
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Downloaded",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
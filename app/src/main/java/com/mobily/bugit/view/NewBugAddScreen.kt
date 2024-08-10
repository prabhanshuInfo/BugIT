package com.mobily.bugit.view

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

class NewBugAddScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AddBugScreen(onBackClicked = {
                finish()
            })
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun AddScreen() {
        MyScreen()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddBugScreen(onBackClicked: () -> Unit) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add New Bug") },
                    navigationIcon = {
                        IconButton(onClick = { onBackClicked() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    modifier = Modifier
                        .background(Color.Blue)
                        .shadow(12.dp)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                MyScreen()
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun MyScreen() {
        var selectImages by remember { mutableStateOf(listOf<Uri>()) }

        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
                selectImages = it
            }
        var textState by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Select File")
                }
                LazyVerticalGrid(GridCells.Fixed(3)) {
                    items(selectImages) { uri ->
                        Image(
                            painter = rememberImagePainter(uri),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(16.dp, 8.dp)
                                .size(100.dp)
                                .clickable {

                                }
                        )
                    }
                }
            }

            // Input field below the row
            TextField(
                value = textState,
                onValueChange = { textState = it },
                label = { Text("Enter Bug Details") },
                modifier = Modifier
                    .fillMaxWidth()
                    .size(0.dp, 300.dp)
            )

            // Button at the bottom
            Button(
                onClick = { /* TODO: Handle button click */ },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            ) {
                Text("Submit")
            }
        }
    }
}
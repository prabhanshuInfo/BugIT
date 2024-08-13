package com.mobily.bugit.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.viewModel.AddBugViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        AddBugFields { _, _ ->

        }
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
                AddBugFields { images, description ->
                    UploadImage(uris = images, description = description)
                }

            }
        }
    }

    @Composable
    fun AddBugFields(onUploadButton: @Composable (images: List<Uri>, description: String) -> Unit) {
        var callUploadTask by remember { mutableStateOf(false) }
        var selectImages by remember { mutableStateOf(listOf<Uri>()) }
        var buttonEnabled by remember { mutableStateOf(false) }
        var textState by remember { mutableStateOf("") }

        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
                selectImages = it
                buttonEnabled = textState.isNotEmpty()
            }
        handleIncomingImage(intent)?.let { uri ->
            selectImages = listOf(uri)
            buttonEnabled = textState.isNotEmpty()
        }

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
                            painter = rememberAsyncImagePainter(uri),
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

            TextField(
                value = textState,
                onValueChange = { textState = it
                    buttonEnabled = selectImages.isNotEmpty() && textState.isNotEmpty()},
                label = { Text("Enter Bug Details") },
                modifier = Modifier
                    .fillMaxWidth()
                    .size(0.dp, 300.dp)
            )

            Button(
                enabled = buttonEnabled,
                onClick = { callUploadTask = true },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            ) {
                Text("Submit")
            }
        }

        if (callUploadTask) {
            onUploadButton(selectImages, textState)
        }
    }

    @Composable
    private fun UploadImage(uris: List<Uri>, description: String) {
        val addBugViewModel: AddBugViewModel = hiltViewModel()
        var showDialog by remember { mutableStateOf(true) }

        if (showDialog) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator()
                }
            }
            val uri = Uri.parse(uris[0].toString())
            val imageName = uri.lastPathSegment ?: "Unknown"
            val ref: StorageReference = FirebaseStorage.getInstance().getReference().child("images_$imageName")
            ref.putFile(uris[0]).addOnSuccessListener { taskSnapshot ->
                Toast.makeText(applicationContext, "File Uploaded Successfully", Toast.LENGTH_LONG).show()
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    showDialog = false
                    addBugViewModel.addValueToNotionPage("$it", description)
                    val bugEntity = BugEntity(0,"$it", description)
                    addBugViewModel.insertBug(bugEntity)
                    finish()
                }

            }.addOnFailureListener {
                showDialog = false
                Toast.makeText(applicationContext, "File Upload Failed...", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setContent {
            AddBugScreen(onBackClicked = {
                finish()
            })
        }
    }

    private fun handleIncomingImage(intent: Intent): Uri? {
        return if (intent.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        } else {
            null
        }
    }
}
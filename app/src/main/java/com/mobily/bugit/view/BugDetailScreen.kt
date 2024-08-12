package com.mobily.bugit.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mobily.bugit.R
import com.mobily.bugit.database.entity.BugEntity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("NotConstructor")
@AndroidEntryPoint
class BugDetailScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BugDetailScreen {
                finish()
            }
        }
    }

    @Composable
    @Preview
    fun Preview() {
        BugDetailScreen {
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BugDetailScreen(onBackClicked: () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Bug Detail") },
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
                BugDetail()
            }
        }
    }

    @Composable
    fun BugDetail() {
        val data = getIntentData()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = data?.imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.placeholder)
                            error(R.drawable.placeholder)
                        }).build()
                ),
                contentDescription = data?.bugDescription,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            data?.bugDescription?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }

    private fun getIntentData(): BugEntity? {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getSerializableExtra("BugData", BugEntity::class.java)
        } else {
            intent?.getSerializableExtra("BugData") as? BugEntity
        }
        return data
    }

}


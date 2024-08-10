package com.mobily.bugit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobily.bugit.model.BugData
import com.mobily.bugit.ui.theme.BugItTheme
import com.mobily.bugit.utils.CommanComponent
import com.mobily.bugit.view.BugDetailScreen
import com.mobily.bugit.view.NewBugAddScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BugItTheme {
                MainScreenLayout(onAddClicked = {
                    startActivity(Intent(this, NewBugAddScreen::class.java))
                }, onItemClick = {
                    val intent = Intent(this, BugDetailScreen::class.java)
                    intent.putExtra("BugData", it)
                    startActivity(intent)
                })
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MainScreenLayout(onAddClicked = {}, onItemClick = {})
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreenLayout(onAddClicked: () -> Unit, onItemClick: (BugData) -> Unit) {
        Scaffold(topBar = {
            TopAppBar(title = { Text("BugIT") }, actions = {
                IconButton(onClick = { onAddClicked() }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }, modifier = Modifier
                .background(Color.Blue)
                .shadow(12.dp)
            )
        }) { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(CommanComponent.sampleCardDataList) { data ->
                    MainRecycleViewItem(bugData = data) {
                        onItemClick(it)
                    }
                }
            }
        }
    }

    @Composable
    fun MainRecycleViewItem(bugData: BugData, onItemClick: (BugData) -> Unit) {
        Column {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(8.dp)
                .clickable {
                    onItemClick(bugData)
                }) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "Description of the image",
                        modifier = Modifier.size(70.dp).align(Alignment.CenterVertically)
                    )
                    Text(
                        text = bugData.title, modifier = Modifier
                            .fillMaxSize().align(Alignment.CenterVertically).padding(0.dp,16.dp,16.dp,16.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
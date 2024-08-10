package com.mobily.bugit.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.mobily.bugit.MainActivity
import com.mobily.bugit.R
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreen: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen{
                this.startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    @SuppressLint("NotConstructor")
    @Composable
    fun SplashScreen(onSplashEnd: () -> Unit = {}) {
        val alpha = remember { Animatable(1f) }

        // Start decreasing the alpha value after 3000ms (3 seconds)
        LaunchedEffect(key1 = true) {
            delay(3000)
            alpha.animateTo(targetValue = 0f, animationSpec = tween(durationMillis = 1000))
            onSplashEnd()
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = R.string.app_name.toString(),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha.value }
            )
        }
    }
}

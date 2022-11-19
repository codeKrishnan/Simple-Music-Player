package com.codekrishnan.simplemusicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codekrishnan.simplemusicplayer.ui.screen.TrackPlayerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackPlayerScreen()
        }
    }
}

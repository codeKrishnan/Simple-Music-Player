package com.codekrishnan.simplemusicplayer.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codekrishnan.simplemusicplayer.ui.theme.BlackBackground
import com.codekrishnan.simplemusicplayer.ui.wigdet.AudioPlayer
import com.codekrishnan.simplemusicplayer.ui.wigdet.TrackBanner

@Composable
fun TrackPlayerScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = BlackBackground
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            TrackBanner(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center)
            )
            AudioPlayer(
                modifier = Modifier
                    .padding(
                        bottom = 32.dp
                    )
                    .align(
                        Alignment.BottomCenter
                    )
            )
        }
    }
}

@Preview
@Composable
private fun TrackPlayerScreenPreview() {
    TrackPlayerScreen()
}
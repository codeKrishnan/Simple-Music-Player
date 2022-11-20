package com.codekrishnan.simplemusicplayer.ui.wigdet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

@Composable
fun AudioPlayer(
    modifier: Modifier = Modifier,
) {
    var isPlaybackPaused by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer
            .Builder(context)
            .build().apply {
                repeatMode = Player.REPEAT_MODE_OFF
                setMediaItem(
                    MediaItem.fromUri(
                        "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba-online-audio-converter.com_-1.wav"
                    )
                )
                playWhenReady = true
                prepare()
            }
    }

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.playWhenReady = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (!isPlaybackPaused) {
                        exoPlayer.playWhenReady = true
                    }
                }
                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.run {
                        stop()
                        release()
                    }
                }
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
    AudioControllerWidget(
        modifier = Modifier
            .then(modifier),
        isPlaybackPaused = isPlaybackPaused,
        onTrackPause = {
            isPlaybackPaused = true
            exoPlayer.pause()
        },
        onTrackResume = {
            isPlaybackPaused = false
            exoPlayer.play()
        }
    )
}

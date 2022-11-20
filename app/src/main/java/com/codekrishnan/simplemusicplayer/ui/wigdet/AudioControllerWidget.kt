package com.codekrishnan.simplemusicplayer.ui.wigdet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codekrishnan.simplemusicplayer.R
import com.codekrishnan.simplemusicplayer.ui.theme.BlackBackground
import com.codekrishnan.simplemusicplayer.ui.theme.Grey
import com.codekrishnan.simplemusicplayer.ui.theme.White
import com.codekrishnan.simplemusicplayer.ui.theme.Yellow


@Composable
fun AudioControllerWidget(
    modifier: Modifier = Modifier,
    isPlaybackPaused: Boolean,
    onTrackPause: () -> Unit,
    onTrackResume: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlackBackground)
            .heightIn(min = 40.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { }
        ) {
            Icon(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.audio_controller_mini_icon_size)),
                painter = painterResource(id = R.drawable.shuffle),
                contentDescription = "",
                tint = Grey
            )
        }
        IconButton(
            onClick = { }
        ) {
            Icon(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.audio_controller_mini_icon_size)),
                painter = painterResource(id = R.drawable.previous_icon),
                contentDescription = "",
                tint = White
            )
        }
        IconButton(
            onClick = {
                if (isPlaybackPaused) {
                    onTrackResume()
                } else {
                    onTrackPause()
                }
            },
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.audio_controller_playback_bg_radius))
                .background(
                    shape = CircleShape,
                    color = Yellow
                )
        ) {
            val icon = if (isPlaybackPaused) {
                painterResource(id = R.drawable.play_icon)
            } else {
                painterResource(id = R.drawable.pause)
            }
            Icon(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.audio_controller_large_icon_size)),
                painter = icon,
                contentDescription = "",
                tint = Color.Black
            )
        }
        IconButton(onClick = { }) {
            Icon(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.audio_controller_mini_icon_size)),
                painter = painterResource(id = R.drawable.next_icon),
                contentDescription = "",
                tint = White
            )
        }
        IconButton(onClick = { }) {
            Icon(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.audio_controller_mini_icon_size)),
                painter = painterResource(id = R.drawable.repeat_icon),
                contentDescription = "",
                tint = Grey
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AudioControllerWidget(
        isPlaybackPaused = false,
        onTrackPause = {},
        onTrackResume = {}
    )
}
package com.codekrishnan.simplemusicplayer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codekrishnan.simplemusicplayer.R
import com.codekrishnan.simplemusicplayer.data.entity.Song
import com.codekrishnan.simplemusicplayer.ui.theme.BlackBackground
import com.codekrishnan.simplemusicplayer.ui.theme.White

@Composable
fun TrackListScreen(
    songs: List<Song>,
    onClick: (Song) -> Unit,
) {
    Surface {
        TrackListWidget(
            songs = songs,
            onClick = onClick
        )
    }
}

@Composable
fun TrackListWidget(
    songs: List<Song>,
    onClick: (Song) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(BlackBackground)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 12.dp
            )
        ) {
            items(songs) { song ->
                TrackShortDetails(
                    song = song,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun TrackShortDetails(
    song: Song,
    onClick: (Song) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
            .clickable {
                onClick(song)
            },
    ) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clip(
                    shape = RoundedCornerShape(12.dp),
                )
                .size(
                    100.dp
                ),
            model = song.imageUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.headphone_bg)
        )
        Text(
            modifier = Modifier
                .align(Alignment.Top)
                .padding(
                    vertical = 8.dp,
                    horizontal = 12.dp
                ),
            text = song.title,
            color = White,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif
            )
        )
    }
}

@Preview
@Composable
fun TrackShortDetailsPreview() {
    TrackShortDetails(
        song = Song(
            mediaId = "",
            title = "Marimba",
            songUrl = "",
            imageUrl = ""
        )
    ) {}
}

@Preview
@Composable
fun TrackListWidgetPreview() {
    TrackListWidget(
        songs = listOf(
            Song(
                mediaId = "",
                title = "Marimba",
                songUrl = "",
                imageUrl = ""
            ),
            Song(
                mediaId = "",
                title = "Marimba",
                songUrl = "",
                imageUrl = ""
            ),
            Song(
                mediaId = "",
                title = "Marimba",
                songUrl = "",
                imageUrl = ""
            )
        )
    ) {}
}
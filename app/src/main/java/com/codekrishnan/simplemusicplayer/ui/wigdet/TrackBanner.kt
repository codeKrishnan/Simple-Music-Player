package com.codekrishnan.simplemusicplayer.ui.wigdet

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codekrishnan.simplemusicplayer.R

@Composable
fun TrackBanner(
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(38.dp))
            .then(modifier)
    ) {
        Image(painter = painterResource(
            id = R.drawable.headphone_bg),
            contentDescription = null
        )
    }
}


@Preview
@Composable
private fun TrackBannerPreview() {
    Surface() {
        TrackBanner()
    }
}
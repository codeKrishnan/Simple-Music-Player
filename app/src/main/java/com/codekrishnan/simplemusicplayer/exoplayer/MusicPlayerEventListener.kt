package com.codekrishnan.simplemusicplayer.exoplayer

import android.app.Service
import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class MusicPlayerEventListener(
    private val musicService: MusicService,
) : Player.Listener {

    @Deprecated("Deprecated in Java")
    override fun onPlayerStateChanged(
        playWhenReady: Boolean,
        playbackState: Int,
    ) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (playbackState == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(Service.STOP_FOREGROUND_DETACH)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(
            musicService,
            "An unknown error occurred: ${error.message}",
            Toast.LENGTH_SHORT
        ).show()
    }
}
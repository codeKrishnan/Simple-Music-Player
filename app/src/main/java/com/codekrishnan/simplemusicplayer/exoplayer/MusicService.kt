package com.codekrishnan.simplemusicplayer.exoplayer

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SERVICE_TAG = "MusicService"
private const val MEDIA_ROOT_ID = "root_id"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    companion object {
        var currentSongDuration = 0L
            private set
    }

    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    private lateinit var musicNotificationManager: MusicNotificationManager

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    private var currentPlayingSong: MediaMetadataCompat? = null

    private var isPlayerInitialized = false

    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            firebaseMusicSource.fetchMediaData()
        }
        val activityPendingIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let { launchIntent ->
            PendingIntent.getActivity(
                this,
                0,
                launchIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        mediaSession = MediaSessionCompat(
            this,
            SERVICE_TAG,
        ).apply {
            setSessionActivity(activityPendingIntent)
            isActive = true
        }
        sessionToken = mediaSession.sessionToken
        musicNotificationManager = MusicNotificationManager(
            context = this,
            sessionToken = mediaSession.sessionToken,
            notificationListener = MusicPlayerNotificationListener(this),
            newSongCallback = {
                currentSongDuration = exoPlayer.duration
            }
        )

        val musicPlaybackPreparer = MusicPlaybackPreparer(
            firebaseMusicSource = firebaseMusicSource,
        ) { preparedMedia ->
            currentPlayingSong = preparedMedia
            preparePlayer(
                songs = firebaseMusicSource.songs,
                itemToPlay = preparedMedia,
                shouldPlayNow = true
            )
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setPlayer(exoPlayer)
            setPlaybackPreparer(musicPlaybackPreparer)
            setQueueNavigator(MusicQueueNavigator())
        }
        musicPlayerEventListener = MusicPlayerEventListener(musicService = this)
        exoPlayer.addListener(musicPlayerEventListener)
        musicNotificationManager.showNotification(player = exoPlayer)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()

        exoPlayer.removeListener(musicPlayerEventListener)
        exoPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot {
        return BrowserRoot(
            MEDIA_ROOT_ID,
            null
        )
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSend = firebaseMusicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(
                            firebaseMusicSource.asMediaItems().toMutableList()
                        )
                        if (!isPlayerInitialized && firebaseMusicSource.songs.isNotEmpty()) {
                            preparePlayer(
                                songs = firebaseMusicSource.songs,
                                itemToPlay = firebaseMusicSource.songs.first(),
                                shouldPlayNow = false
                            )
                            isPlayerInitialized = true
                        }
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultsSend) {
                    result.detach()
                }
            }
        }
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        shouldPlayNow: Boolean,
    ) {
        val currentSongIndex = if (currentPlayingSong != null) {
            songs.indexOf(itemToPlay)
        } else {
            0
        }
        exoPlayer.prepare(
            firebaseMusicSource.asConcatenatingMediaSource(
                dataSourceFactory = dataSourceFactory
            )
        )
        exoPlayer.seekTo(currentSongIndex, 0)
        exoPlayer.playWhenReady = shouldPlayNow
    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {

        override fun getMediaDescription(
            player: Player,
            windowIndex: Int,
        ): MediaDescriptionCompat {
            return firebaseMusicSource.songs[windowIndex].description
        }

    }
}
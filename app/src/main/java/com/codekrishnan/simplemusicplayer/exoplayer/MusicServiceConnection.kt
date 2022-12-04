package com.codekrishnan.simplemusicplayer.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codekrishnan.simplemusicplayer.other.Event
import com.codekrishnan.simplemusicplayer.other.Resource

const val NETWORK_ERROR = "NETWORK_ERROR"

class MusicServiceConnection(
    context: Context,
) {

    private val _isConnected = MutableLiveData<Event<Resource<Boolean>>>()
    val isConnected: LiveData<Event<Resource<Boolean>>>
        get() = _isConnected

    private val _networkError = MutableLiveData<Event<Resource<Boolean>>>()
    val networkError: LiveData<Event<Resource<Boolean>>>
        get() = _networkError

    private val _playbackState = MutableLiveData<PlaybackStateCompat?>()
    val playbackState: LiveData<PlaybackStateCompat?>
        get() = _playbackState

    private val _currentlyPlayingSong = MutableLiveData<MediaMetadataCompat?>()
    val currentlyPlayingSong: LiveData<MediaMetadataCompat?>
        get() = _currentlyPlayingSong

    lateinit var mediaController: MediaControllerCompat

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            MusicService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply {
        connect()
    }

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    fun subscribe(
        parentId: String,
        callback: MediaBrowserCompat.SubscriptionCallback,
    ) {
        mediaBrowser.subscribe(
            parentId,
            callback
        )
    }

    fun unsubscribe(
        parentId: String,
        callback: MediaBrowserCompat.SubscriptionCallback,
    ) {
        mediaBrowser.unsubscribe(
            parentId,
            callback
        )
    }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context,
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            ).apply {
                registerCallback(MediaControllerCallback())
            }

            _isConnected.value = Event(
                Resource.success(true)
            )
        }

        override fun onConnectionSuspended() {
            _isConnected.value = Event(
                Resource.error(
                    message = "The connection was suspended.",
                    data = false
                )
            )
        }

        override fun onConnectionFailed() {
            _isConnected.value = Event(
                Resource.error(
                    message = "Couldn't connect to media browser.",
                    data = false,
                )
            )
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _playbackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _currentlyPlayingSong.value = metadata
        }

        override fun onSessionEvent(
            event: String?,
            extras: Bundle?,
        ) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_ERROR -> {
                    _networkError.value = Event(
                        Resource.error(
                            "couldn't connect to the server.",
                            data = null
                        )
                    )
                }
                else -> Unit
            }
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }

}
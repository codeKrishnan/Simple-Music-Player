package com.codekrishnan.simplemusicplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codekrishnan.simplemusicplayer.data.entity.Song
import com.codekrishnan.simplemusicplayer.exoplayer.MEDIA_ROOT_ID
import com.codekrishnan.simplemusicplayer.exoplayer.MusicServiceConnection
import com.codekrishnan.simplemusicplayer.exoplayer.isPlayEnabled
import com.codekrishnan.simplemusicplayer.exoplayer.isPlaying
import com.codekrishnan.simplemusicplayer.exoplayer.isPrepared
import com.codekrishnan.simplemusicplayer.other.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private val _mediaItems = MutableLiveData<Resource<List<Song>>>()
    val mediaItems: LiveData<Resource<List<Song>>>
        get() = _mediaItems

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val currentPlayingSong = musicServiceConnection.currentlyPlayingSong
    val playbackState = musicServiceConnection.playbackState

    init {
        _mediaItems.postValue(
            Resource.loading(data = null)
        )
        musicServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>,
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.map { mediaItem ->
                        Song(
                            mediaId = mediaItem.mediaId!!,
                            title = mediaItem.description.title.toString(),
                            songUrl = mediaItem.description.mediaUri.toString(),
                            imageUrl = mediaItem.description.iconUri.toString()
                        )
                    }
                    _mediaItems.value = Resource.success(items)
                }
            }
        )
    }

    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPrevious() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(position: Long) {
        musicServiceConnection.transportControls.seekTo(position)
    }

    fun playOrToggleSong(
        mediaItem: Song,
        toggle: Boolean = false,
    ) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId == currentPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> {
                        if (toggle) {
                            musicServiceConnection.transportControls.pause()
                        }
                    }
                    playbackState.isPlayEnabled -> {
                        musicServiceConnection.transportControls.play()
                    }
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(
                mediaItem.mediaId,
                null
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : SubscriptionCallback() {

            }
        )
    }
}
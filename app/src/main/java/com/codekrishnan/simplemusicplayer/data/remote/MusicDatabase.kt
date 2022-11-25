package com.codekrishnan.simplemusicplayer.data.remote

import com.codekrishnan.simplemusicplayer.data.entity.Song
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicDatabase {

    companion object {
        private const val SONG_COLLECTION = "songs"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (exception: Exception) {
            emptyList()
        }
    }
}
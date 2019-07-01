package com.example.photovideoclockframe.presentation.extensions

import android.media.MediaPlayer
import android.view.SurfaceView

fun SurfaceView.playVideo(path: String, mediaPlayer: MediaPlayer) {
    mediaPlayer.apply {
        try {
            setDataSource(path)
            isLooping = true
            setDisplay(this@playVideo.holder)
            setOnPreparedListener {
                start()
            }
            prepareAsync()
        } catch (e: Exception) { /* TODO add timber logging */ }
    }
}
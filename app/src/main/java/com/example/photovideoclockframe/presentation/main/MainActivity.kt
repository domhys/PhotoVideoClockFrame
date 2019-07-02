package com.example.photovideoclockframe.presentation.main

import android.content.ContentResolver
import android.media.MediaPlayer
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.photovideoclockframe.R
import com.example.photovideoclockframe.presentation.base.BaseView
import com.example.photovideoclockframe.presentation.extensions.loadAndRotateImage
import com.example.photovideoclockframe.presentation.extensions.playVideo
import com.example.photovideoclockframe.utility.MediaPathLoader
import com.example.photovideoclockframe.utility.permissions.PermissionsManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseView<MainContract.Presenter>(), MainContract.View {

    override lateinit var presenter: MainContract.Presenter
    override val resolver: ContentResolver
        get() = this.contentResolver

    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runOnUiThread {  }
        hideToolbar()
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, PermissionsManager(this), MediaPathLoader()) //TODO Dagger
    }

    private fun hideToolbar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun loadNewMedia(path: String, mediaType: MEDIA_TYPE) {
        stopMediaPlayer()
        if (mediaType == MEDIA_TYPE.PHOTO) {
            loadPhoto(path)
        } else {
           loadVideo(path)
        }
    }

    private fun stopMediaPlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }

    private fun loadPhoto(path: String) {
        surfaceView.isInvisible = true
        appCompatImageView.isVisible = true
        appCompatImageView.loadAndRotateImage(path)
    }

    private fun loadVideo(path: String) {
        appCompatImageView.isInvisible = true
        surfaceView.isVisible = true
        surfaceView.playVideo(path, mediaPlayer)
    }

    override fun setCurrentTime() {
        analogClock.setCurrentTime()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
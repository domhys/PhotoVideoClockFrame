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
import com.example.photovideoclockframe.presentation.main.di.DaggerMainComponent
import com.example.photovideoclockframe.presentation.main.di.MainModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseView<MainContract.Presenter>(), MainContract.View {

    @Inject override lateinit var presenter: MainContract.Presenter
    override val resolver: ContentResolver
        get() = this.contentResolver

    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeActivityFullscreen()
        setContentView(R.layout.activity_main)
        createMenu()
        setOnClickListeners()
    }

    override fun createGraph() {
        DaggerMainComponent.builder()
            .mainModule(MainModule(this))
            .build()
            .inject(this)
    }

    private fun createMenu() {
        toolbar.inflateMenu(R.menu.settings_menu)
        toolbar.setOnMenuItemClickListener { item ->
            changeToolbarVisibility()
            if (item?.itemId == R.id.settings) {
                presenter.settingsClicked(this)
                true
            } else false
        }
    }

    private fun setOnClickListeners() {
        snackBarContainer.setOnClickListener {
            changeToolbarVisibility()
        }
    }

    private fun changeToolbarVisibility() {
        toolbar.isVisible = !toolbar.isVisible
    }

    private fun makeActivityFullscreen() {
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

    override fun showErrorSnackBarWithAction(messageResId: Int, callback: () -> Unit) {
        showSnackBarWithAction(snackBarContainer, getString(messageResId), action = callback)
    }
}
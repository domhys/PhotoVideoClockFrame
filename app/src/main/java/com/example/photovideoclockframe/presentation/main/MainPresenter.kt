package com.example.photovideoclockframe.presentation.main

import com.example.photovideoclockframe.R
import com.example.photovideoclockframe.presentation.base.BasePresenter
import com.example.photovideoclockframe.utility.MediaPathLoader
import com.example.photovideoclockframe.utility.permissions.PermissionsManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainPresenter(
    private val mainView: MainContract.View,
    private val permissionsManager: PermissionsManager,
    private val mediaPathLoader: MediaPathLoader
) : BasePresenter(), MainContract.Presenter {

    private val mediaPaths = mutableListOf<Pair<String,MEDIA_TYPE>>()
    private var clockTicking = false
    private var imagesChanging = false

    override fun onBind() {
        mainView.setCurrentTime()
        updateClockEverySecond()
        doOrRequestReadPermissions { loadImages() }
    }

    private fun updateClockEverySecond() {
        if (clockTicking) return
        register(
            Observable.interval(1, TimeUnit.SECONDS)
                .subscribe({ mainView.setCurrentTime() }, {})
        )
        clockTicking = true
    }

    private fun loadImages() {
        loadImagesPaths()
        initiateMediaChanges()
    }

    private fun loadImagesPaths() {
        mediaPaths.addAll(mediaPathLoader.loadMediaPaths(mainView.resolver))
    }

    private fun initiateMediaChanges() {
        if (imagesChanging) return
        var imageIndex = 0
        register(
            Observable.interval(DEFAULT_MEDIA_CHANGE_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe({
                    mediaPaths[imageIndex].let { media ->
                        mainView.loadNewMedia(media.first, media.second)
                    }
                    imageIndex = (imageIndex + 1) % mediaPaths.size
                }, {
                    it.printStackTrace()
                })
        )
        imagesChanging = true
    }

    private inline fun doOrRequestReadPermissions(action: () -> Unit) {
        if (permissionsManager.readExternalStorageGranted()) {
            action()
        } else {
            permissionsManager.requestReadExternalStoragePermission(REQUEST_READ_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionsManager.readExternalStorageGranted()) loadImages()
        else mainView.showErrorSnackBarWithAction(R.string.permission_needed) { doOrRequestReadPermissions { loadImages() } }
    }

    companion object {
        private const val REQUEST_READ_PERMISSION_CODE = 412
        private const val DEFAULT_MEDIA_CHANGE_INTERVAL_SECONDS = 2L
    }
}

enum class MEDIA_TYPE { PHOTO, VIDEO }
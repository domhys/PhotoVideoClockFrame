package com.example.photovideoclockframe.presentation.main

import android.app.AlertDialog
import android.content.Context
import com.example.photovideoclockframe.R
import com.example.photovideoclockframe.presentation.base.BasePresenter
import com.example.photovideoclockframe.utility.MediaPathLoader
import com.example.photovideoclockframe.utility.permissions.PermissionsManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MainPresenter(
    private val mainView: MainContract.View,
    private val permissionsManager: PermissionsManager,
    private val mediaPathLoader: MediaPathLoader
) : BasePresenter(), MainContract.Presenter {

    private val mediaPaths = mutableListOf<Pair<String, MEDIA_TYPE>>()
    private var clockTicking = false
    private var mediaChangeInterval = DEFAULT_MEDIA_CHANGE_INTERVAL_SECONDS
    private var mediaChangeDisposable: Disposable? = null
    private var imageIndex = 0

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
        mediaChangeDisposable?.dispose()
        mediaChangeDisposable = Observable.interval(mediaChangeInterval, TimeUnit.SECONDS)
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
        mediaChangeDisposable?.let { register(it) }
    }

    override fun settingsClicked(context: Context) {
        createDisplayTimeDialog(context)
    }

    private fun createDisplayTimeDialog(context: Context) {
        AlertDialog.Builder(context)
            .setSingleChoiceItems(
                R.array.duration_options, -1
            ) { _, which ->
                mediaChangeInterval = DEFAULT_MEDIA_CHANGE_INTERVAL_SECONDS + which
            }
            .setTitle(R.string.display_time_title)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                initiateMediaChanges()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
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
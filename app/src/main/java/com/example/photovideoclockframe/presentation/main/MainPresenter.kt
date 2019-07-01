package com.example.photovideoclockframe.presentation.main

import com.example.photovideoclockframe.utility.permissions.Permissions
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainPresenter(
    private val mainView: MainContract.View,
    private val permissions: Permissions
) : MainContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun onBind() {
        mainView.setCurrentTime()
        updateClockEverySecond()
        loadNextImage() //TODO show toast if user doesn't grant permission
    }

    private fun updateClockEverySecond() {
        compositeDisposable.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .subscribe({ mainView.setCurrentTime() }, {})
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    private fun loadNextImage() {
        doOrRequestReadPermissions {  }
    }

    private inline fun doOrRequestReadPermissions(action: () -> Unit) {
        if (permissions.readExternalStorageGranted()) {
            action()
        } else {
            permissions.requestReadExternalStoragePermission(REQUEST_READ_PERMISSION_CODE)
        }
    }

    companion object {
        private const val REQUEST_READ_PERMISSION_CODE = 412
    }
}
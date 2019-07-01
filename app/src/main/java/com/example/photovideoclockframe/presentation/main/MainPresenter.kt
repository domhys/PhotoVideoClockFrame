package com.example.photovideoclockframe.presentation.main

import android.util.Log
import com.example.photovideoclockframe.utility.permissions.PermissionsManager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import android.provider.MediaStore
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainPresenter(
    private val mainView: MainContract.View,
    private val permissionsManager: PermissionsManager
) : MainContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun onBind() {
        mainView.setCurrentTime()
        updateClockEverySecond()
        doOrRequestReadPermissions { loadImagesPaths() } //TODO show toast if user doesn't grant permission
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

    private fun loadImagesPaths() {
        val galleryImageUrls: ArrayList<String> = ArrayList()
        val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)//get all columns of type images
        val orderBy = MediaStore.Images.Media.DATE_TAKEN//order data by date
        val cursor = mainView.resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,null,null,orderBy)
        while (cursor?.moveToNext() == true) {
            galleryImageUrls.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)))
        }
        galleryImageUrls.reverse()
        cursor?.close()
        var iterator = 0
        compositeDisposable.add(
        Observable.interval(6, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mainView.loadNewImage(galleryImageUrls[iterator])
                iterator = (iterator + 1) % galleryImageUrls.size
            },{
                it.printStackTrace()
            })
        )
    }

    private inline fun doOrRequestReadPermissions(action: () -> Unit) {
        if (permissionsManager.readExternalStorageGranted()) {
            action()
        } else {
            permissionsManager.requestReadExternalStoragePermission(REQUEST_READ_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionsManager.readExternalStorageGranted()) loadImagesPaths()
    }

    companion object {
        private const val REQUEST_READ_PERMISSION_CODE = 412
    }
}
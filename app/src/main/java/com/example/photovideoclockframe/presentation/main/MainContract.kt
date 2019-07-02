package com.example.photovideoclockframe.presentation.main

import android.content.ContentResolver
import android.content.Context
import com.example.photovideoclockframe.presentation.IBasePresenter
import com.example.photovideoclockframe.presentation.IBaseView

interface MainContract {

    interface View : IBaseView<Presenter> {
        val resolver: ContentResolver
        fun setCurrentTime()
        fun loadNewMedia(path: String, mediaType: MEDIA_TYPE)
        fun showErrorSnackBarWithAction(messageResId: Int, callback: () -> Unit)
    }

    interface Presenter: IBasePresenter {
        fun settingsClicked(context: Context)
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    }
}
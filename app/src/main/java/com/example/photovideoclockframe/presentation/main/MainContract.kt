package com.example.photovideoclockframe.presentation.main

import android.content.ContentResolver
import android.net.Uri
import com.example.photovideoclockframe.presentation.BasePresenter
import com.example.photovideoclockframe.presentation.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {
        val resolver: ContentResolver
        fun setCurrentTime()
        fun loadNewImage(path: String)
    }

    interface Presenter: BasePresenter {
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    }
}
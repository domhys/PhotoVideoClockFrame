package com.example.photovideoclockframe.presentation.main

import com.example.photovideoclockframe.presentation.BasePresenter
import com.example.photovideoclockframe.presentation.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {
        fun setCurrentTime()
    }

    interface Presenter: BasePresenter {

    }
}
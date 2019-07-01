package com.example.photovideoclockframe.presentation.base

import androidx.appcompat.app.AppCompatActivity
import com.example.photovideoclockframe.presentation.IBasePresenter
import com.example.photovideoclockframe.presentation.IBaseView

abstract class BaseView<T : IBasePresenter> : AppCompatActivity(), IBaseView<T> {

    abstract override val presenter: T

    override fun onStart() {
        super.onStart()
        presenter.onBind()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}
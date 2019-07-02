package com.example.photovideoclockframe.presentation.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.photovideoclockframe.R
import com.example.photovideoclockframe.presentation.IBasePresenter
import com.example.photovideoclockframe.presentation.IBaseView
import com.google.android.material.snackbar.Snackbar

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

    fun showSnackBarWithAction(
        parentView: View,
        message: String,
        duration: Int = Snackbar.LENGTH_INDEFINITE,
        action: () -> Unit
    ) {
        Snackbar.make(parentView, message, duration).setAction(R.string.ask_again) { action() }.show()
    }
}
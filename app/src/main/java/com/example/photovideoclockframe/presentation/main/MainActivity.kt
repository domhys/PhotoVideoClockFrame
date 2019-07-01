package com.example.photovideoclockframe.presentation.main

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.photovideoclockframe.R
import com.example.photovideoclockframe.utility.permissions.Permissions
import kotlinx.android.synthetic.main.activity_main.*

//TODO extract to BASE
class MainActivity : AppCompatActivity(), MainContract.View {

    override lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, Permissions(this)) //TODO Dagger
    }

    private fun hideToolbar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun setCurrentTime() {
        analogClock.setCurrentTime()
    }

    override fun onStart() {
        super.onStart()
        presenter.onBind()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}

package com.example.photovideoclockframe.presentation.main

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.photovideoclockframe.R
import com.example.photovideoclockframe.utility.permissions.PermissionsManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.graphics.Matrix


//TODO extract to BASE
class MainActivity : AppCompatActivity(), MainContract.View {

    override lateinit var presenter: MainContract.Presenter
    override val resolver: ContentResolver
        get() = this.contentResolver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, PermissionsManager(this)) //TODO Dagger
    }

    private fun hideToolbar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun loadNewImage(path: String) {
        Glide.with(this)
            .asBitmap()
            .load(File(path))
            .transition(BitmapTransitionOptions.withCrossFade(1000))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object:CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    if (resource.height > resource.width) {
                        appCompatImageView.setImageBitmap(rotateBitmap(resource, -90f))
                    } else {
                        appCompatImageView.setImageBitmap(resource)
                    }
                }

            })
    }

    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

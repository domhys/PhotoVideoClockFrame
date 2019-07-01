package com.example.photovideoclockframe.presentation.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

fun ImageView.loadAndRotateImage(path: String) {
    if (path.endsWith(".gif")) {
        Glide.with(this)
            .load(File(path))
            .into(this)
    } else {
        Glide.with(this)
            .asBitmap()
            .load(File(path))
            .transition(BitmapTransitionOptions.withCrossFade(1000))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    if (resource.height > resource.width) {
                        this@loadAndRotateImage.setImageBitmap(resource.rotate(-90f))
                    } else {
                        this@loadAndRotateImage.setImageBitmap(resource)
                    }
                }
            })
    }
}
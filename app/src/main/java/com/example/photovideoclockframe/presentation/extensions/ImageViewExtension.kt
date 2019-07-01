package com.example.photovideoclockframe.presentation.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

fun ImageView.loadAndRotateImage(path: String) {
    if (path.endsWith(".gif")) {
        Glide.with(this)
            .load(File(path))
            .transition(DrawableTransitionOptions.withCrossFade(2000))
            .into(this)
    } else {
        Glide.with(this)
            .asBitmap()
            .load(File(path))
            .transition(BitmapTransitionOptions.withCrossFade(2000))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    var wasResourceChanged = false
                    val correctOrientationBitmap = if (resource.height > resource.width) resource.rotate(-90f) else resource
                    transition?.let {
                        wasResourceChanged = transition.transition(correctOrientationBitmap, BitmapImageViewTarget(this@loadAndRotateImage))
                    }
                    if (!wasResourceChanged) this@loadAndRotateImage.setImageBitmap(correctOrientationBitmap)
                }
            })
    }
}
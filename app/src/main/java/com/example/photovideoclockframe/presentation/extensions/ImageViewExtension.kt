package com.example.photovideoclockframe.presentation.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.photovideoclockframe.utility.RotateIfNeededTransformation
import java.io.File

fun ImageView.loadAndRotateImage(path: String) {
    if (path.endsWith(".gif")) {
        Glide.with(this)
            .load(File(path))
            .transition(DrawableTransitionOptions.withCrossFade(2000))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
    } else {
        Glide.with(this)
            .asBitmap()
            .load(File(path))
            .transition(BitmapTransitionOptions.withCrossFade(2000))
            .transform(RotateIfNeededTransformation())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
    }
}
package com.example.photovideoclockframe.presentation.extensions

import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.photovideoclockframe.R
import com.example.photovideoclockframe.utility.RotateIfNeededTransformation
import java.io.File

fun ImageView.loadAndRotateImage(path: String) {
    if (path.endsWith(".gif")) {
        Glide.with(this)
            .load(File(path))
            .transition(DrawableTransitionOptions.withCrossFade(DEFAULT_CROSS_FADE_DURATION_MILLIS))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
    } else {
        Glide.with(this)
            .asBitmap()
            .load(File(path))
            .transition(BitmapTransitionOptions.withCrossFade(DEFAULT_CROSS_FADE_DURATION_MILLIS))
//            .transition(GenericTransitionOptions.with(R.anim.small_to_large)) Example of a customizable animation from xml
            .transform(RotateIfNeededTransformation())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
    }
}

private const val DEFAULT_CROSS_FADE_DURATION_MILLIS = 2000
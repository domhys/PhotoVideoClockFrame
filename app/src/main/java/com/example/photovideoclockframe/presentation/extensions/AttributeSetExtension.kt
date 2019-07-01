package com.example.photovideoclockframe.presentation.extensions

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet

fun AttributeSet.extractAttributes(context: Context, attributes: IntArray, applyAttributes: (TypedArray)-> Unit) {
    val typedArray = context.obtainStyledAttributes(this, attributes, 0, 0)
    try {
        applyAttributes(typedArray)
    } finally {
        typedArray.recycle()
    }
}

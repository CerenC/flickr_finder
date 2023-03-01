package com.cerenb.flickrfinder.ui.extensions

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun DividerItemDecoration.withCustomDrawable(
    context: Context,
    @DrawableRes drawableId: Int
): DividerItemDecoration {
    ContextCompat.getDrawable(context, drawableId)?.let {
        setDrawable(it)
    }
    return this
}

fun RecyclerView.clearItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}
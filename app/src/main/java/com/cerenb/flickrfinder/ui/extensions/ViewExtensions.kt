package com.cerenb.flickrfinder.ui.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cerenb.flickrfinder.R

fun ImageView.loadImage(url: String, context: Context) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}
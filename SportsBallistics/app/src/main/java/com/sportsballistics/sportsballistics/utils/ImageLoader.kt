package com.sportsballistics.sportsballistics.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

inline fun loadImage(
    imageUrl: String,
    imageview: ImageView
) {
    Glide.with(imageview).load(imageUrl).into(imageview)
}


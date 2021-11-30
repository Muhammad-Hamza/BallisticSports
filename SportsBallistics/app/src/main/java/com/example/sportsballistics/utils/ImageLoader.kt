package com.example.sportsballistics.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.sportsballistics.R

inline fun loadImage(
    imageUrl: String,
    imageview: ImageView
) {
    Glide.with(imageview).load(imageUrl).into(imageview)
}


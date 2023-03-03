package com.codegenetics.extensions.extension

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun ImageView.colorFilter(@ColorRes colorId: Int) {
    setColorFilter(ContextCompat.getColor(context, colorId))
}

fun ImageView.loadImageWithGlide(url: Any?) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadImageWithBlurEffect(image: Any) {
    Glide.with(this)
        .load(image)
        .apply(RequestOptions().override(10).centerCrop())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}
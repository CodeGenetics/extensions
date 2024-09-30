package com.codegenetics.extensions.extension

import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
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

fun ImageView.rotate() {
    val animator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
    animator.duration = 1000
    animator.repeatCount = ObjectAnimator.INFINITE
    animator.interpolator = LinearInterpolator()
    animator.start()
}
package com.codegenetics.extensions.extension

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.google.android.material.bottomsheet.BottomSheetDialog

fun BottomSheetDialog.setSize() {
    val window = this.window
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val width = this.context.getScreenWidth(100.0)
    val height = MATCH_PARENT
    window?.setLayout(width, height)
}
package com.codegenetics.extensions.extension

import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton

fun CompoundButton.checked() {
    this.isChecked = true
}

fun CompoundButton.unChecked(){
    this.isChecked = false
}

fun RadioButton.checked(){
    this.isChecked = true
}
fun RadioButton.unChecked(){
    this.isChecked = false
}
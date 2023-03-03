package com.codegenetics.extensions.extension

fun String.isValidColor(): Boolean {
    if (this.length < 3) return false
    if (this.matches("^#([0-9a-fA-F]{6})$".toRegex())) {
        return true
    } else if (this.matches("^#([0-9a-fA-F]{3})$".toRegex())) {
        return true

    } else if (this.matches("^#([0-9a-fA-F]{8})$".toRegex())) {
        return true
    }
    return false
}

fun String.validateColor(): String {
    if (this.length < 3) return ""
    if (this.matches("^#([0-9a-fA-F]{6})$".toRegex())) {
        return this
    } else if (this.matches("^#([0-9a-fA-F]{3})$".toRegex())) {
        return this

    } else if (this.matches("^#([0-9a-fA-F]{8})$".toRegex())) {
        return this
    }
    return "${this}0"
}
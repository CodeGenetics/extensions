package com.codegenetics.extensions.helper

import androidx.annotation.Keep

@Keep
enum class ShareType {
    NONE, WHATSAPP, FACEBOOK, GMAIL, SKYPE, INSTAGRAM, SNAPCHAT, TWITTER
}

enum class ColorPick{
    VIBRANT,VIBRANT_LIGHT,
    VIBRANT_DARK,
    MUTED,
    MUTED_LIGHT,
    MUTED_DARK,
    DOMINANT
}
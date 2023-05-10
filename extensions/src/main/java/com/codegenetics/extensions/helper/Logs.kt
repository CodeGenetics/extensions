package com.codegenetics.extensions.helper

import android.util.Log
import com.codegenetics.extensions.lib.BuildConfig

class Logs {
    companion object {
        fun e(tag: String?, msg: String, tr: Throwable?) {
            if (BuildConfig.BUILD_TYPE == "debug") Log.e(tag, "JARVIS: $msg", tr)
        }

        fun e(tag: String?, msg: String) {
            if (BuildConfig.BUILD_TYPE == "debug") Log.e(tag, "JARVIS: $msg")
        }

        fun d(tag: String?, msg: String) {
            if (BuildConfig.BUILD_TYPE == "debug") Log.d(tag, "JARVIS: $msg")
        }
        fun i(tag: String?, msg: String) {
            if (BuildConfig.BUILD_TYPE == "debug") Log.i(tag, "JARVIS: $msg")
        }

        fun w(tag: String?, msg: String) {
            if (BuildConfig.BUILD_TYPE == "debug") Log.w(tag, "JARVIS: $msg")
        }

        fun w(tag: String?, msg: String, tr: Throwable?) {
            if (BuildConfig.BUILD_TYPE == "debug") Log.w(tag, "JARVIS: $msg", tr)
        }
    }
}
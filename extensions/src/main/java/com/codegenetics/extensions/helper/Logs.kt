package com.codegenetics.extensions.helper

import android.util.Log
import com.codegenetics.extensions.lib.BuildConfig

class Logs {
    companion object {
        fun e(tag: String?, msg: String, tr: Throwable?) {
             Log.e(tag, "JARVIS: $msg", tr)
        }

        fun e(tag: String?, msg: String) {
             Log.e(tag, "JARVIS: $msg")
        }

        fun d(tag: String?, msg: String) {
             Log.d(tag, "JARVIS: $msg")
        }
        fun i(tag: String?, msg: String) {
             Log.i(tag, "JARVIS: $msg")
        }

        fun w(tag: String?, msg: String) {
             Log.w(tag, "JARVIS: $msg")
        }

        fun w(tag: String?, msg: String, tr: Throwable?) {
             Log.w(tag, "JARVIS: $msg", tr)
        }
    }
}
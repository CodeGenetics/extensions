package com.codegenetics.extensions.extension

import android.os.SystemClock
import android.widget.Chronometer

/** set chronometer to 0
 * set text to [hh:mm:ss]*/
fun Chronometer.init() {
    this.show()
    this.base = SystemClock.elapsedRealtime()
    this.setOnChronometerTickListener { chronometer: Chronometer ->
        val time = SystemClock.elapsedRealtime() - chronometer.base
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - h * 3600000 - m * 60000).toInt() / 1000
        val hh = if (h < 10) "0$h" else h.toString() + ""
        val mm = if (m < 10) "0$m" else m.toString() + ""
        val ss = if (s < 10) "0$s" else s.toString() + ""
        chronometer.text = "$hh:$mm:$ss"
    }
}


/** starts chronometer from zero
 * in (hh:mm:ss) format*/
fun Chronometer.initAndStart() {
    this.show()
    this.base = SystemClock.elapsedRealtime()
    this.setOnChronometerTickListener { chronometer: Chronometer ->
        val time = SystemClock.elapsedRealtime() - chronometer.base
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - h * 3600000 - m * 60000).toInt() / 1000
        val hh = if (h < 10) "0$h" else h.toString() + ""
        val mm = if (m < 10) "0$m" else m.toString() + ""
        val ss = if (s < 10) "0$s" else s.toString() + ""
        chronometer.text = "$hh:$mm:$ss"
    }
    this.start()
}

fun Chronometer.startWithZero() {
    this.base = SystemClock.elapsedRealtime()
    this.start()
}

fun Chronometer.resume(timeWhenStopped: Long) {
    this.base = SystemClock.elapsedRealtime() + timeWhenStopped;
    this.start()
}

fun Chronometer.getTime(): Long {
    return this.base - SystemClock.elapsedRealtime()
}

package com.codegenetics.extensions.utils

import android.graphics.Paint
import android.text.style.ReplacementSpan

class ThickerStrikethroughSpan(private val thickness: Float) : ReplacementSpan() {
    override fun getSize(
        paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?
    ) = paint.measureText(text, start, end).toInt()

    override fun draw(
        canvas: android.graphics.Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val textStr = text.subSequence(start, end).toString()
        canvas.drawText(textStr, x, y.toFloat(), paint)

        val originalStrokeWidth = paint.strokeWidth
        val originalStyle = paint.style

        paint.strokeWidth = thickness
        paint.style = Paint.Style.STROKE

        val strikeY = (y + paint.fontMetrics.ascent + paint.fontMetrics.descent) / 2
        canvas.drawLine(x, strikeY, x + paint.measureText(textStr), strikeY, paint)

        paint.strokeWidth = originalStrokeWidth
        paint.style = originalStyle
    }
}
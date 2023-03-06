package com.codegenetics.extensions.extension.customviews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.codegenetics.extensions.lib.R


class InlineImageTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {
    private var imageResourceId: Int = 0
    private var imgIndexInText: Int = 0
    private var imgClickHandler: Runnable? = null
    private var imgVerticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.InlineImageTextView)
        imageResourceId = attributes.getResourceId(
            R.styleable.InlineImageTextView_inlineImgResourceId,
            imageResourceId
        )
        imgIndexInText = attributes.getInt(
            R.styleable.InlineImageTextView_inlineImgIndexInText,
            imgIndexInText
        )
        imgVerticalAlignment = attributes.getInt(
            R.styleable.InlineImageTextView_inlineImageVerticalAlign,
            imgVerticalAlignment
        )
        attributes.recycle()

        refresh()
    }

    private fun refresh() {
        setTextAndInlineImage(
            textView = this,
            imgResId = imageResourceId,
            imgAlign = imgVerticalAlignment,
            text = this.text.toString(),
            atIndex = imgIndexInText,
            imgClickHandler
        )
    }

    fun setImageClickHandler(handler: Runnable) {
        this.imgClickHandler = handler
        refresh()
    }

    fun setInlineImage(
        imgResId: Int,
        imgAlign: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
        text: String? = null,
        atIndex: Int? = null,
        clickHandler: Runnable? = null
    ) {
        setTextAndInlineImage(this, imgResId, imgAlign, text, atIndex, clickHandler)
    }

    fun setInlineImage(
        bitmap: Bitmap,
        imgAlign: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
        text: String? = null,
        atIndex: Int? = null,
        clickHandler: Runnable? = null
    ) {
        setTextAndInlineImage(this, bitmap, imgAlign, text, atIndex, clickHandler)
    }

    companion object {
        fun setTextAndInlineImage(
            textView: TextView,
            imgResId: Int,
            imgAlign: Int = DynamicDrawableSpan.ALIGN_CENTER,
            text: String? = null,
            atIndex: Int? = null,
            clickHandler: Runnable? = null
        ) {
            if (imgResId <= 0) {
                return
            }

            val originalText: String = text ?: "${textView.text}"
            val index: Int = atIndex ?: originalText.length

            if (index >= originalText.length) {
                return
            }

            val addSpace = "${originalText.substring(0, index)} ${originalText.substring(index)}"
            val spanString = SpannableString(addSpace)

            spanString.setSpan(
                ImageSpan(textView.context, imgResId, imgAlign),
                index,
                index + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            clickHandler?.let { handler ->
                spanString.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(p0: View) {
                            handler.run()
                        }
                    },
                    index,
                    index + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textView.movementMethod = LinkMovementMethod.getInstance()
            }

            textView.text = spanString
        }

        fun setTextAndInlineImage(
            textView: TextView,
            uri: Uri,
            imgAlign: Int = DynamicDrawableSpan.ALIGN_CENTER,
            text: String? = null,
            atIndex: Int? = null,
            clickHandler: Runnable? = null
        ) {
            if (uri.toString() == "") {
                return
            }

            val originalText: String = text ?: "${textView.text}"
            val index: Int = atIndex ?: originalText.length

            if (index >= originalText.length) {
                return
            }

            val addSpace = "${originalText.substring(0, index)} ${originalText.substring(index)}"
            val spanString = SpannableString(addSpace)

            spanString.setSpan(
                ImageSpan(textView.context, uri, imgAlign),
                index,
                index + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            clickHandler?.let { handler ->
                spanString.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(p0: View) {
                            handler.run()
                        }
                    },
                    index,
                    index + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textView.movementMethod = LinkMovementMethod.getInstance()
            }

            textView.text = spanString
        }

        fun setTextAndInlineImage(
            textView: TextView,
            bitmap: Bitmap,
            imgAlign: Int = DynamicDrawableSpan.ALIGN_CENTER,
            text: String? = null,
            atIndex: Int? = null,
            clickHandler: Runnable? = null
        ) {

            val originalText: String = text ?: "${textView.text}"
            val index: Int = atIndex ?: originalText.length

            if (index >= originalText.length) {
                return
            }

            val addSpace = "${originalText.substring(0, index)} ${originalText.substring(index)}"
            val spanString = SpannableString(addSpace)

            spanString.setSpan(
                ImageSpan(textView.context, bitmap, imgAlign),
                index,
                index + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            clickHandler?.let { handler ->
                spanString.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(p0: View) {
                            handler.run()
                        }
                    },
                    index,
                    index + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textView.movementMethod = LinkMovementMethod.getInstance()
            }

            textView.text = spanString
        }

    }



}
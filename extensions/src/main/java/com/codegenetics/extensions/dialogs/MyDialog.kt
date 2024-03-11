package com.codegenetics.extensions.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.codegenetics.extensions.extension.changeBackgroundColor
import com.codegenetics.extensions.extension.getScreenWidth
import com.codegenetics.extensions.extension.gone
import com.codegenetics.extensions.extension.hide
import com.codegenetics.extensions.extension.isNotEmptyAndBlank
import com.codegenetics.extensions.extension.loadImageWithGlide
import com.codegenetics.extensions.extension.show
import com.codegenetics.extensions.extension.textColor
import com.codegenetics.extensions.lib.R
import com.codegenetics.extensions.lib.databinding.DialogOneButtonBinding
import com.codegenetics.extensions.lib.databinding.DialogTwoButtonBinding
import com.codegenetics.extensions.lib.databinding.DialogWithIconBinding
import com.codegenetics.extensions.lib.databinding.ViewDialogTwoButtonModernBinding
import com.codegenetics.extensions.lib.databinding.ViewDialogTwoButtonModernNoTitleHorizontalBinding
import com.codegenetics.extensions.lib.databinding.ViewDialogTwoButtonModernNoTitleVerticalBinding

/** Simple Two Button Dialog
 * by default two text horizontal aligned
 * */
fun Activity.showTwoButtonDialog(
        title: String = "",
        msg: String,
        titleRightButton: String = "OK",
        titleLeftButton: String = "Cancel",
        @ColorRes textColorRightButton: Int = R.color.red,
        @ColorRes textColorLeftButton: Int = R.color.colorPrimary,
        @DrawableRes bgRightButton: Int = 0,
        @DrawableRes bgLeftButton: Int = 0,
        @ColorRes backgroundColor:Int = R.color.background,
        @DrawableRes background:Int = R.drawable.dialog_layout,
        @StyleRes theme: Int = R.style.AppDialog,
        callback: (Boolean, Dialog) -> Unit
    ) {
    val dialog = Dialog(this, 0)
        val binding = DialogTwoButtonBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)

        binding.apply {
            if (title.isEmpty() || title.isBlank()) {
                tvTitle.gone()
            }
            viewDialog.setBackgroundResource(background)
            viewDialog.changeBackgroundColor(backgroundColor)
            tvTitle.text = title
            tvMessage.text = msg
            btnRight.text = titleRightButton
            btnLeft.text = titleLeftButton
            btnRight.textColor(textColorRightButton)
            btnLeft.textColor(textColorLeftButton)
            btnRight.setBackgroundResource(bgRightButton)
            btnLeft.setBackgroundResource(bgLeftButton)
            btnLeft.setOnClickListener { callback(false, dialog) }
            btnRight.setOnClickListener { callback(true, dialog) }


        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

/** Two Buttons vertically aligned
 * Positive button will be filled
 * cancel button will be Text Simple*/
fun Activity.showTwoButtonModernDialog(
        title: String = "",
        msg: String,
        titlePositive: String = "OK",
        titleNegative: String = "Cancel",
        @ColorRes textColorPositiveButton: Int = R.color.white,
        @ColorRes textColorNegativeButton: Int = R.color.dark_grey,
        @DrawableRes bgPositiveButton: Int = R.drawable.primary_bg,
        @DrawableRes bgNegativeButton: Int = R.drawable.bg_transparent,
        @ColorRes backgroundColor:Int = R.color.background,
        @DrawableRes background:Int = R.drawable.dialog_layout,
        @StyleRes theme: Int = R.style.AppDialog,
        callback: (Boolean, Dialog) -> Unit
    ) {
    val dialog = Dialog(this, theme)
        val binding = ViewDialogTwoButtonModernBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)


        binding.apply {
            viewDialog.setBackgroundResource(background)
            viewDialog.changeBackgroundColor(backgroundColor)
            tvTitle.text = title
            tvMessage.text = msg
            btnPositive.text = titlePositive
            btnNegative.text = titleNegative
            btnPositive.textColor(textColorPositiveButton)
            btnNegative.textColor(textColorNegativeButton)
            btnPositive.setBackgroundResource(bgPositiveButton)
            btnNegative.setBackgroundResource(bgNegativeButton)
            btnPositive.setOnClickListener { callback(true, dialog) }
            btnNegative.setOnClickListener { callback(false, dialog) }

        }
        dialog.show()
        val width = getScreenWidth(90.0)
        dialog.window?.setLayout(width, WRAP_CONTENT)

    }

/** No Title
 * Message Aligned on left side
 * two button Horizontal*/
fun Activity.showTwoButtonModernDialogNoTitleVertical(
        msg: String,
        titlePositive: String = "OK",
        titleNegative: String = "Cancel",
        @ColorRes textColorPositiveButton: Int = R.color.white,
        @ColorRes textColorNegativeButton: Int = R.color.white,
        @DrawableRes bgPositiveButton: Int = R.drawable.primary_bg,
        @DrawableRes bgNegativeButton: Int = R.drawable.bg_red,
        @StyleRes theme: Int = R.style.AppDialog,
        shouldShowCloseButton: Boolean = false,
        callback: (Boolean, Dialog) -> Unit
    ) {
    val dialog = Dialog(this, theme)
        val binding = ViewDialogTwoButtonModernNoTitleVerticalBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)


        binding.apply {
            if (shouldShowCloseButton) {
                ivClose.show()
            } else {
                ivClose.hide()
            }
            tvTitle.text = msg

            if (titlePositive.isNotEmptyAndBlank()) {
                btnPositive.text = titlePositive
                btnPositive.textColor(textColorPositiveButton)
                btnPositive.setBackgroundResource(bgPositiveButton)
            } else {
                btnPositive.gone()
            }

            if (titleNegative.isNotEmptyAndBlank()) {
                btnNegative.text = titleNegative
                btnNegative.textColor(textColorNegativeButton)
                btnNegative.setBackgroundResource(bgNegativeButton)
            } else {
                btnNegative.gone()
            }



            btnPositive.setOnClickListener { callback(true, dialog) }
            btnNegative.setOnClickListener { callback(false, dialog) }
            ivClose.setOnClickListener { dialog.dismiss() }

        }
        dialog.show()
        val width = getScreenWidth(80.0)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(width, WRAP_CONTENT)

    }


@Deprecated("You can use [showTwoButtonDialog] with Customization")
fun Activity.showTwoButtonModernDialogNoTitleHorizontal(
        msg: String ,
        titlePositive: String = "OK",
        titleNegative: String = "Cancel",
        @ColorRes textColorPositiveButton: Int = R.color.white,
        @ColorRes textColorNegativeButton: Int = R.color.white,
        @DrawableRes bgPositiveButton: Int = R.drawable.primary_bg,
        @DrawableRes bgNegativeButton: Int = R.drawable.bg_red,
        @StyleRes theme: Int = R.style.AppDialog,
        shouldShowCloseButton: Boolean = false,
        callback: (Boolean, Dialog) -> Unit
    ) {
    val dialog = Dialog(this, theme)
        val binding =
            ViewDialogTwoButtonModernNoTitleHorizontalBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)


        binding.apply {
            if (shouldShowCloseButton) {
                ivClose.show()
            } else {
                ivClose.hide()
            }
            tvTitle.text = msg
            btnPositive.text = titlePositive
            btnNegative.text = titleNegative
            btnPositive.textColor(textColorPositiveButton)
            btnNegative.textColor(textColorNegativeButton)
            btnPositive.setBackgroundResource(bgPositiveButton)
            btnNegative.setBackgroundResource(bgNegativeButton)
            btnPositive.setOnClickListener { callback(true, dialog) }
            btnNegative.setOnClickListener { callback(false, dialog) }
            ivClose.setOnClickListener { dialog.dismiss() }

        }
        dialog.show()
        val width = getScreenWidth(80.0)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(width, WRAP_CONTENT)

    }

/** Simple One Button Dialog
 * default button is Text*/
fun Activity.showOneButtonDialog(
        title: String = "",
        msg: String,
        titleButton: String = "OK",
        @ColorRes btnTextColor: Int = R.color.colorPrimary,
        @DrawableRes btnDrawable: Int = R.drawable.bg_transparent,
        @ColorRes backgroundColor:Int = R.color.background,
        @DrawableRes background:Int = R.drawable.dialog_layout,
        callback: (Dialog) -> Unit
    ): Dialog {
        val dialog = Dialog(this)
        val binding = DialogOneButtonBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)

        binding.apply {
            viewDialog.setBackgroundResource(background)
            viewDialog.changeBackgroundColor(backgroundColor)
            if (title.isNotEmptyAndBlank()) {
                tvTitle.text = title
            } else {
                tvTitle.gone()
            }

            if (msg.isNotEmptyAndBlank()) {
                tvMessage.text = msg
            } else {
                tvMessage.gone()
            }

            btnOk.text = titleButton
            btnOk.textColor(btnTextColor)
            btnOk.setBackgroundResource(btnDrawable)
            btnOk.setOnClickListener { callback(dialog) }
        }

        dialog.show()
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

fun Activity.showDialogWithImage(
        title: String = "",
        msg: String = "",
        titleButton: String = "",
        icon: Any = 0,
        @ColorRes colorButton: Int = R.color.colorPrimary,
        @DrawableRes btnDrawable: Int = R.drawable.bg_transparent,
        @ColorRes backgroundColor:Int = R.color.background,
        @DrawableRes background:Int = R.drawable.dialog_layout,
        @StyleRes theme: Int = R.style.AppDialog,
        callback: (Dialog) -> Unit
    ) {
    val dialog = Dialog(this, theme)
        val binding = DialogWithIconBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)

        binding.apply {
            viewDialog.setBackgroundResource(background)
            viewDialog.changeBackgroundColor(backgroundColor)
            if (title.isNotEmptyAndBlank()) {
                tvTitle.text = title
            } else {
                tvTitle.gone()
            }

            if (msg.isNotEmptyAndBlank()) {
                tvMessage.text = msg
            } else {
                tvMessage.gone()
            }

            if (icon == 0) {
                ivIcon.gone()
            } else {
                ivIcon.show()
                ivIcon.loadImageWithGlide(icon)
            }

            if (titleButton.isNotEmptyAndBlank()) {
                btnOk.text = titleButton
                btnOk.textColor(colorButton)
                btnOk.setBackgroundResource(btnDrawable)
            } else {
                btnOk.gone()
            }


            ivClose.setOnClickListener { dialog.dismiss() }
            btnOk.setOnClickListener { callback(dialog) }
        }

        dialog.show()
        val width = getScreenWidth(90.0)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(width, WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


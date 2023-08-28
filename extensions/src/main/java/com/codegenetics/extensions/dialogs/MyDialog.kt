package com.codegenetics.extensions.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.codegenetics.extensions.extension.*
import com.codegenetics.extensions.lib.R
import com.codegenetics.extensions.lib.databinding.*
    fun Activity.showTwoButtonDialog(
        title: String = "",
        msg: String,
        titleRightButton: String = "OK",
        titleLeftButton: String = "Cancel",
        @ColorRes colorRightButton: Int = R.color.red,
        @ColorRes colorLeftButton: Int = R.color.colorPrimary,
        @ColorRes backgroundColor:Int = R.color.background,
        @DrawableRes background:Int = R.drawable.dialog_layout,
        callback: (Boolean, Dialog) -> Unit
    ) {
        val dialog = Dialog(this)
        val binding = DialogTwoButtonBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)

        binding.apply {
            viewDialog.setBackgroundResource(background)
            viewDialog.changeBackgroundColor(backgroundColor)
            tvTitle.text = title
            tvMessage.text = msg
            btnRight.text = titleRightButton
            btnLeft.text = titleLeftButton
            btnRight.textColor(colorRightButton)
            btnLeft.textColor(colorLeftButton)
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
        callback: (Boolean, Dialog) -> Unit
    ) {
        val dialog = Dialog(this)
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


    fun Activity.showTwoButtonModernDialogNoTitleVertical(
        msg: String,
        titlePositive: String = "OK",
        titleNegative: String = "Cancel",
        @ColorRes textColorPositiveButton: Int = R.color.white,
        @ColorRes textColorNegativeButton: Int = R.color.white,
        @DrawableRes bgPositiveButton: Int = R.drawable.primary_bg,
        @DrawableRes bgNegativeButton: Int = R.drawable.bg_red,
        callback: (Boolean, Dialog) -> Unit
    ) {
        val dialog = Dialog(this, R.style.AppDialog)
        val binding = ViewDialogTwoButtonModernNoTitleVerticalBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)


        binding.apply {

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



    fun Activity.showTwoButtonModernDialogNoTitleHorizontal(
        msg: String ,
        titlePositive: String = "OK",
        titleNegative: String = "Cancel",
        @ColorRes textColorPositiveButton: Int = R.color.white,
        @ColorRes textColorNegativeButton: Int = R.color.white,
        @DrawableRes bgPositiveButton: Int = R.drawable.primary_bg,
        @DrawableRes bgNegativeButton: Int = R.drawable.bg_red,
        callback: (Boolean, Dialog) -> Unit
    ) {
        val dialog = Dialog(this, R.style.AppDialog)
        val binding =
            ViewDialogTwoButtonModernNoTitleHorizontalBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)


        binding.apply {
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


    fun Activity.showOneButtonDialog(
        title: String = "",
        msg: String,
        titleButton: String = "OK",
        @ColorRes colorButton: Int = R.color.colorPrimary,
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
            btnOk.textColor(colorButton)
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
        @DrawableRes icon: Int = 0,
        @ColorRes colorButton: Int = R.color.colorPrimary,
        @DrawableRes btnDrawable: Int = R.drawable.bg_transparent,
        @ColorRes backgroundColor:Int = R.color.background,
        @DrawableRes background:Int = R.drawable.dialog_layout,
        callback: (Dialog) -> Unit
    ) {
        val dialog = Dialog(this)
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


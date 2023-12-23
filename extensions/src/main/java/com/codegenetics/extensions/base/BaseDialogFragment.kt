package com.codegenetics.extensions.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.codegenetics.extensions.lib.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseDialogFragment<B : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B
) : DialogFragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var _binding: B? = null
    val binding get() = _binding

    open fun onViewBindingCreated(savedInstanceState: Bundle?) {}
    open fun B.bindViews() {}
    open fun B.bindListeners() {}
    open fun B.loadData() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        isCancelable = false
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            onViewBindingCreated(savedInstanceState)
            binding?.run {
                bindViews()
                bindListeners()
                loadData()
            }
        }
    }

    override fun getTheme() = R.style.ThemeDialog

    override fun onDestroyView() {
        coroutineContext[Job]?.cancel()
        super.onDestroyView()
    }

}

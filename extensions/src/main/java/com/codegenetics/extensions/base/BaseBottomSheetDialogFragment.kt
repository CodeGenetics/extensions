package com.my_constants.bases

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import com.codegenetics.extensions.helper.LocaleHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.Locale

abstract class BaseBottomSheetDialogFragment<B : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B
) : BottomSheetDialogFragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var _binding: B? = null
    val binding get() = _binding

    open fun onViewBindingCreated(savedInstanceState: Bundle?) {}
    open fun B.bindViews() {}
    open fun B.bindListeners() {}
    open fun B.bindObservers() {}
    open fun B.loadData() {}
    open fun B.loadAds() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        dialog?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            val language = LocaleHelper.getLanguage(it)
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration = Configuration(resources.configuration)
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        _binding = bindingInflater.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {

            try {

                onViewBindingCreated(savedInstanceState)

                view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val dialog = dialog as BottomSheetDialog
                        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                        val behavior = BottomSheetBehavior.from(bottomSheet!!)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                })

                binding?.run {
                    bindViews()
                    bindListeners()
                    bindObservers()

                    loadData()
                    loadAds()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    override fun onDestroyView() {
        coroutineContext[Job]?.cancel()
        super.onDestroyView()
    }

}

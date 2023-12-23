package com.my_constants.bases

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.codegenetics.extensions.extension.checkPermissionRationale
import com.codegenetics.extensions.extension.requestPermission
import com.codegenetics.extensions.extension.setStatusBarColor
import com.codegenetics.extensions.lazyAndroid
import com.codegenetics.extensions.lib.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseActivity<B : ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> B
) : AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    val binding: B by lazyAndroid { bindingFactory(layoutInflater) }


     val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) binding.onPermissionGranted() else binding.onPermissionDenied()
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = onBackPress()
    }

    open fun onViewBindingCreated(savedInstanceState: Bundle?) {}
    open fun B.bindViews() {}
    open fun B.bindListeners() {}
    open fun B.bindObservers() {}
    open fun B.loadData() {}
    open fun B.loadAds() {}
    open fun B.onPermissionGranted() {}
    open fun B.onPermissionDenied() {}

    open fun onBackPress() {
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        with(binding) {
            setContentView(root)

            onViewBindingCreated(savedInstanceState)

            bindViews()
            bindListeners()
            bindObservers()
            loadData()
            loadAds()


        }

    }

    @CallSuper
    override fun onDestroy() {
        coroutineContext[Job]?.cancel()
        super.onDestroy()
    }

}
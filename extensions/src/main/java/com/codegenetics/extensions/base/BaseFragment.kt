package com.my_constants.bases

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.codegenetics.extensions.extension.isAlive
import com.codegenetics.extensions.helper.Logs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseFragment<B : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B
) : Fragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var _binding: B? = null
    val binding get() = _binding

    open fun onViewBindingCreated(savedInstanceState: Bundle?) {}
    open fun B.onAnimationEnded() {}
    open fun B.bindViews() {}
    open fun B.bindListeners() {}
    open fun B.bindObservers() {}
    open fun B.loadData() {}
    open fun B.loadAds() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            onViewBindingCreated(savedInstanceState)
            binding?.run {
                bindViews()
                bindListeners()
                bindObservers()

                loadData()
                loadAds()
            }
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        var anim: Animation? = null
        isAlive {
            anim = AnimationUtils.loadAnimation(it, nextAnim)

            anim?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    Logs.d("TAG", "Animation started.")
                    // additional functionality
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    Logs.d("TAG", "Animation repeating.")
                    // additional functionality
                }

                override fun onAnimationEnd(animation: Animation?) {
                    Logs.d("TAG", "Animation ended.")
                    binding?.onAnimationEnded()
                }
            })

        }
        return anim ?: super.onCreateAnimation(transit, enter, nextAnim)
    }


    override fun onDestroyView() {
        coroutineContext[Job]?.cancel()
        super.onDestroyView()
    }

}

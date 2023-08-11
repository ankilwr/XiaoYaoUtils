package com.mellivora.base.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.mellivora.base.expansion.childFragmentManager
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


abstract class BaseCoreDialog: DialogFragment() {

    private val resultKey by lazy { "${javaClass.name}: onResult()" }
    private val dismissKey by lazy { "${javaClass.name}: onDismiss()" }


    private var isFirstLazyFlag = true
    override fun onResume() {
        super.onResume()
        if(isFirstLazyFlag){
            isFirstLazyFlag = false
            onLazyLoad()
        }
    }


    /**
     * 数据懒加载
     */
    open fun onLazyLoad() {}


    fun setOnDismissListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, onDismiss:(()->Unit)? = null){
        manager.setFragmentResultListener(dismissKey, lifecycleOwner){ _, _ ->
            onDismiss?.invoke()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        setFragmentResult(dismissKey, Bundle())
        super.onDismiss(dialog)
    }

    fun showNow(fragmentManager: FragmentManager){
        showNow(fragmentManager, this::class.java.name)
    }

    open fun showNow(lifecycleOwner: LifecycleOwner, fragmentManager: FragmentManager, tag: String = this::class.java.name, listener: FragmentResultListener){
        fragmentManager.setFragmentResultListener(resultKey, lifecycleOwner, listener)
        super.showNow(fragmentManager, tag)
    }

    open fun setFragmentResult(bundle: Bundle){
        setFragmentResult(resultKey, bundle)
    }

    open fun dismissForResult(bundle: Bundle){
        setFragmentResult(resultKey, bundle)
        dismissAllowingStateLoss()
    }


}
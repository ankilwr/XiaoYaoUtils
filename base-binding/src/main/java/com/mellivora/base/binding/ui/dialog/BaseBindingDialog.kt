package com.mellivora.base.binding.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.mellivora.base.expansion.childFragmentManager
import com.mellivora.base.ui.dialog.BaseCoreDialog
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


abstract class BaseBindingDialog<T : ViewBinding>: BaseCoreDialog() {

    abstract fun initViews(binding: T)

    var viewBinding: T? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val type: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        val inflate: Method = cls.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
        viewBinding = inflate.invoke(null, inflater, container, false) as T
        if(viewBinding is ViewDataBinding){
            (viewBinding as ViewDataBinding).lifecycleOwner = viewLifecycleOwner
        }
        return viewBinding!!.root
    }

    private var isFirstLazyFlag = true
    override fun onResume() {
        super.onResume()
        if(isFirstLazyFlag){
            isFirstLazyFlag = false
            initViews(viewBinding!!)
        }
    }

    fun setOnDismissListener(view: View, lifecycleOwner: LifecycleOwner, onDismiss:(()->Unit)? = null){
        val fragmentManager = view.childFragmentManager ?: return
        setOnDismissListener(fragmentManager, lifecycleOwner, onDismiss)
    }

    open fun showNow(view: View, tag: String = this::class.java.name){
        val fragmentManager = view.childFragmentManager ?: return
        showNow(fragmentManager, tag)
    }

    open fun showNow(view: View, tag: String = this::class.java.name, listener: FragmentResultListener){
        val fragmentManager = view.childFragmentManager ?: return
        val lifecycleOwner = view.findViewTreeLifecycleOwner() ?: return
        showNow(lifecycleOwner, fragmentManager, tag, listener)
    }

}
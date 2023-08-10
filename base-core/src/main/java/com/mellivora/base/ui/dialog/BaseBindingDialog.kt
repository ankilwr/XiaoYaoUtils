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


abstract class BaseBindingDialog<T : ViewBinding>: DialogFragment() {

    abstract fun initViews(binding: T)

    var viewBinding: T? = null
        private set

    private val resultKey by lazy { "${javaClass.name}: onResult()" }
    private val dismissKey by lazy { "${javaClass.name}: onDismiss()" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val type: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        val inflate: Method = cls.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
        viewBinding = inflate.invoke(null, inflater, container, false) as T
        if(viewBinding is ViewDataBinding){
            (viewBinding as ViewDataBinding).lifecycleOwner = this
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

    fun setOnDismissListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, onDismiss:(()->Unit)? = null){
        manager.setFragmentResultListener(dismissKey, lifecycleOwner){ _, _ ->
            onDismiss?.invoke()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setFragmentResult(dismissKey, Bundle())
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
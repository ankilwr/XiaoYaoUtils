package com.mellivora.base.binding.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mellivora.base.api.LoadingDialogApi
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


open class BaseBindingFragment<T : ViewBinding>: Fragment(), LoadingDialogApi {

    lateinit var viewBinding: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val type: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        val inflate: Method = cls.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
        viewBinding = inflate.invoke(null, inflater, container, false) as T
        if(viewBinding is ViewDataBinding){
            (viewBinding as ViewDataBinding).lifecycleOwner = viewLifecycleOwner
            initBinding(viewBinding)
        }
        return viewBinding.root
    }

    private var isFirstLazyFlag = true
    override fun onResume() {
        super.onResume()
        if(isFirstLazyFlag){
            isFirstLazyFlag = false
            initViews()
            onLazyLoad()
        }
    }

    /**
     * 数据懒加载
     */
    open fun onLazyLoad() {}

    /**
     * 绑定DataBinding
     */
    open fun initBinding(binding: T){

    }

    open fun initViews(){}

}
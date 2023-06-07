package com.mellivora.base.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.mellivora.base.api.LoadingDialogApi
import com.mellivora.base.ui.dialog.LoadingDialog
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * 绑定DataBinding的基础Activity
 */
abstract class BaseBindingActivity<T : ViewBinding>: FragmentActivity(), LoadingDialogApi {

    lateinit var viewBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        val inflate: Method = cls.getDeclaredMethod("inflate", LayoutInflater::class.java)
        viewBinding = inflate.invoke(null, LayoutInflater.from(this)) as T
        setContentView(viewBinding.root)
        if(viewBinding is ViewDataBinding){
            (viewBinding as ViewDataBinding).lifecycleOwner = this
            initBinding(viewBinding)
        }
        initViews()
    }

    abstract fun initBinding(binding: T)

    open fun initViews(){

    }
}

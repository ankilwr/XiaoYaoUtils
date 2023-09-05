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
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import com.mellivora.base.expansion.childFragmentManager
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


abstract class BaseCoreDialog: DialogFragment() {

    private var isFirstLazyFlag = true

    /**
     * 各种缓存处理(屏幕旋转重启后，数据依旧有效，直至viewModel释放)
     */
    private val cacheViewModel: FragmentCacheViewModel by viewModels()

    private var onCancelListener: (() -> Unit)? = null
    private var onDismissListener: (() -> Unit)? = null
    private var onResultListener: ((Bundle) -> Unit)? = null

    class FragmentCacheViewModel: ViewModel(){
        var cacheOnCancelListener: (() -> Unit)? = null
        var cacheOnDismissListener: (() -> Unit)? = null
        var cacheOnResultListener: ((Bundle) -> Unit)? = null
    }

    /**
     * 数据懒加载
     */
    open fun onLazyLoad() {}

    /**
     * 设置取消监听(用户手动取消)
     */
    fun setOnCancelListener(onCancel: (()->Unit)?){
        try {
            cacheViewModel.cacheOnCancelListener = onCancel
        }catch (e: Throwable){
            onCancelListener = onCancel
        }
    }

    /**
     * 设置弹窗消失监听
     */
    fun setOnDismissListener(onDismiss: (()->Unit)?){
        try {
            cacheViewModel.cacheOnDismissListener = onDismiss
        }catch (e: Throwable){
            onDismissListener = onDismiss
        }
    }

    /**
     * 设置弹窗处理结果回调
     */
    fun setOnResultListener(onResult: ((Bundle)->Unit)?){
        try {
            cacheViewModel.cacheOnResultListener = onResult
        }catch (e: Throwable){
            onResultListener = onResult
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCancelListener?.let {
            cacheViewModel.cacheOnCancelListener = it
        }
        onDismissListener?.let {
            cacheViewModel.cacheOnDismissListener = it
        }
        onResultListener?.let {
            cacheViewModel.cacheOnResultListener = it
        }
    }

    override fun onResume() {
        super.onResume()
        if(isFirstLazyFlag){
            isFirstLazyFlag = false
            onLazyLoad()
        }
    }

    fun showNow(fragmentManager: FragmentManager){
        showNow(fragmentManager, this::class.java.name)
    }

    fun showNow(fragmentManager: FragmentManager, tag: String = this::class.java.name, onResult:((Bundle)->Unit)){
        setOnResultListener(onResult)
        showNow(fragmentManager, tag)
    }

    override fun onCancel(dialog: DialogInterface) {
        cacheViewModel.cacheOnCancelListener?.invoke()
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        cacheViewModel.cacheOnDismissListener?.invoke()
        super.onDismiss(dialog)
    }

    open fun setFragmentResult(bundle: Bundle){
        cacheViewModel.cacheOnResultListener?.invoke(bundle)
    }

    open fun dismissForResult(bundle: Bundle){
        setFragmentResult(bundle)
        dismissAllowingStateLoss()
    }

}
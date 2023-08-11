package com.mellivora.base.api

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.mellivora.base.ui.dialog.LoadingDialog
import kotlinx.coroutines.Job

interface LoadingDialogApi {

    private fun getLoadingDialogTag(): String{
        return LoadingDialog::class.java.name
    }

    //为了简化没必要的重复代码，就将该实现逻辑放在这个接口下面了
    fun showLoadingDialog(
        fragmentManager: FragmentManager,
        lifecycleOwner: LifecycleOwner,
        disposable: Job? = null,
        cancelEnable: Boolean = true,
        message: String? = null
    ){
        val dialogTag = getLoadingDialogTag()
        val loadingDialog = fragmentManager.findFragmentByTag(dialogTag) as? LoadingDialog ?: LoadingDialog()
        loadingDialog.apply {
            isCancelable = cancelEnable
            setMessage(message)
            setOnDismissListener(fragmentManager, lifecycleOwner){
                disposable ?: return@setOnDismissListener
                if(disposable.isActive) disposable.cancel()
            }
            showNow(fragmentManager, dialogTag)
        }
    }

    fun dismissLoadingDialog(fragmentManager: FragmentManager){
        val dialogTag = getLoadingDialogTag()
        val loadingDialog = fragmentManager.findFragmentByTag(dialogTag) as? LoadingDialog ?: return
        loadingDialog.dismissAllowingStateLoss()
    }

}

/**
 * 获取一个默认的加载弹窗(提供给java调用)
 */
fun getDefaultLoadingDialogApi(): LoadingDialogApi{
    return object: LoadingDialogApi{  }
}

package com.mellivora.base.vm

import androidx.core.util.Consumer
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.mellivora.base.R
import com.mellivora.base.api.LoadingDialogApi
import com.mellivora.base.exception.ErrorStatus
import com.mellivora.base.expansion.getResString
import com.mellivora.base.expansion.showToast
import com.mellivora.base.exception.parse
import com.mellivora.base.state.LoadingDialogState
import com.mellivora.base.state.LoadingState
import com.mellivora.base.state.PullState
import kotlinx.coroutines.Job

open class LoadingViewModel: BaseViewModel() {

    val pullState = MutableLiveData<PullState>()
    val dialogState = MutableLiveData<LoadingDialogState>()
    var currentPage = 1


    /**
     * 获取当前需要加载的页码
     */
    fun getLoadPage(isRefresh: Boolean, startPage: Int = 1): Int{
        return if(isRefresh) startPage else currentPage+1
    }

    fun loading(stateData: MutableLiveData<PullState> = pullState){
        val state = stateData.value ?: PullState()
        state.loadingState = LoadingState.LOADING
        state.message = getResString(R.string.base_status_loading)
        stateData.value = state
    }

    fun loadingSuccess(stateData: MutableLiveData<PullState> = pullState){
        pullSuccess(true, isPull = false, hasMore = false, stateData = stateData)
    }

    fun loadingError(isPull: Boolean = false, throwable: Throwable? = null, stateData: MutableLiveData<PullState> = pullState){
        if(throwable != null){
            pullErrorConsumer(true, isPull = isPull, stateData).accept(throwable)
        }else{
            pullError(true, isPull = isPull, 0, getResString(R.string.base_status_error), stateData)
        }
    }

    fun pullSuccess(isRefresh: Boolean, isPull: Boolean, hasMore: Boolean, message: String? = "加载成功", stateData: MutableLiveData<PullState> = pullState){
        if(isRefresh){
            currentPage = 1
        }else{
            currentPage += 1
        }
        val result = stateData.value?: PullState()
        result.isRefresh = isRefresh
        result.isPull = isPull
        result.hasMore = hasMore
        result.loadingState = LoadingState.SUCCESS
        result.message = message
        result.code = ErrorStatus.SUCCESS
        stateData.value = result
    }

    fun pullError(isRefresh: Boolean, isPull: Boolean, code: Int, error: String, stateData: MutableLiveData<PullState> = pullState){
        val result = stateData.value?: PullState()
        result.isRefresh = isRefresh
        result.isPull = isPull
        result.hasMore = false
        result.loadingState = LoadingState.ERROR
        result.message = error
        result.code = code
        stateData.value = result
    }

    /**
     * 带有下拉刷线|加载更多的回调，兼容errorConsumer()
     * @param isRefresh: 是否是下拉刷新
     * @see errorConsumer()
     * @return 异常捕获事件
     */
    fun pullErrorConsumer(isRefresh: Boolean = true, isPull: Boolean = false, stateData: MutableLiveData<PullState> = pullState): Consumer<Throwable> {
        return Consumer {
            val error = it.parse()
            if(error.errorCode == ErrorStatus.CANCEL){
                //主动发起的HTTP取消操作(UI在中断HTTP请求前自行处理，此处不在回调出去)
                return@Consumer
            }
            pullError(isRefresh, isPull, error.errorCode, error.errorMsg, stateData)
        }
    }

    /**
     * 逻辑异常的捕获结果处理
     * @param dismissDialog: 是否要关闭加载框
     * @param errorToast：是否弹错误提示Toast
     * @param next：异常结果回调(如需其他操作可传入next回调)
     * @return 异常捕获事件
     */
    fun errorConsumer(dismissDialog: Boolean = true, errorToast: Boolean = true, next: (msg : String) -> Unit = {}): Consumer<Throwable> {
        return Consumer {
            val error = it.parse()
            if(error.errorCode == ErrorStatus.CANCEL){
                //主动发起的HTTP取消操作(UI在中断HTTP请求前自行处理，此处不在回调出去)
                return@Consumer
            }
            //其他异常处理
            if (dismissDialog) dismissLoadingDialog()
            if (errorToast) showToast(error.errorMsg)
            next(error.errorMsg)
        }
    }


    /**
     * 注册加载Dialog
     */
    fun registerLoadingDialog(lifecycleOwner: LifecycleOwner, api: LoadingDialogApi){
        dialogState.observe(lifecycleOwner) {
            val fragmentManager = when(lifecycleOwner){
                is FragmentActivity ->  lifecycleOwner.supportFragmentManager
                is Fragment -> lifecycleOwner.childFragmentManager
                else -> null
            }
            fragmentManager ?: return@observe
            if(it.isShow){
                api.showLoadingDialog(fragmentManager, lifecycleOwner, it.job, it.cancelEnable, it.message)
            }else{
                api.dismissLoadingDialog(fragmentManager)
            }
        }
    }

    fun showLoadingDialog(disposable: Job? = null, cancelEnable: Boolean = true, message: String? = null){
        dialogState.value = (LoadingDialogState(true, disposable, cancelEnable, message))
    }

    fun dismissLoadingDialog(){
        dialogState.value = (LoadingDialogState(false))
    }

}
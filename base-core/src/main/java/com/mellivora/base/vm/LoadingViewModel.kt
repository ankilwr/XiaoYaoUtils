package com.mellivora.base.vm

import androidx.core.util.Consumer
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.mellivora.base.core.R
import com.mellivora.base.api.LoadingDialogApi
import com.mellivora.base.exception.ErrorStatus
import com.mellivora.base.expansion.getResString
import com.mellivora.base.expansion.showToast
import com.mellivora.base.exception.parse
import com.mellivora.base.state.LoadingDialogEvent
import com.mellivora.base.state.LoadingState
import com.mellivora.base.state.PullState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class LoadingViewModel: BaseViewModel() {

    private var currentPage = 1
    val defaultPageSize = 20

    /**
     * loading|pullState状态流
     * 只保留最终事件的通道(适合用做单一状态记录)
     */
    private val _pullState = MutableStateFlow(PullState())
    val pullState = _pullState.asStateFlow()

    /**
     * 消息队列通道(假如有多个订阅者，事件只会由其中一个订阅者处理，即事件只会被消费一次)
     * 例如：弹Toast、导航Fragment等
     */
    private val _loadingDialogChannel: Channel<LoadingDialogEvent> = Channel(Channel.UNLIMITED)
    val loadingDialogChannel= _loadingDialogChannel.receiveAsFlow()

//    /**
//     * 可保留多个事件的通道，并且支持一对多(多个订阅者都能收到通道内的事件回调)
//     */
//    private val _event: MutableSharedFlow<String> = MutableSharedFlow()
//    val event = _event.asSharedFlow()


    /**
     * 数据加载是否为初始状态
     */
    fun isNoneState(): Boolean{
        return pullState.value.loadingState == LoadingState.NONE
    }

    /**
     * 获取当前需要加载的页码
     * @param startPage: 默认页码起始位置【0|1】, 有些接口页码是从1开始的
     */
    fun getLoadPage(isRefresh: Boolean, startPage: Int = 1): Int{
        return if(isRefresh) startPage else currentPage+1
    }

    fun loading(
        isRefresh: Boolean = true,
        isPull: Boolean = false,
        stateData: MutableStateFlow<PullState> = _pullState
    ){
        loadingDialogChannel.buffer()
        val state = stateData.value.copy()
        state.loadingState = LoadingState.LOADING
        state.message = getResString(R.string.base_status_loading)
        state.isRefresh = isRefresh
        state.isPull = isPull
        stateData.value = state
    }

    fun loadingSuccess(stateData: MutableStateFlow<PullState> = _pullState){
        pullSuccess(true, isPull = false, hasMore = false, stateData = stateData)
    }

    fun loadingError(isPull: Boolean = false, throwable: Throwable? = null, stateData: MutableStateFlow<PullState> = _pullState){
        if(throwable != null){
            pullErrorConsumer(true, isPull = isPull, stateData).accept(throwable)
        }else{
            pullError(true, isPull = isPull, 0, getResString(R.string.base_status_error), stateData)
        }
    }

    fun pullSuccess(isRefresh: Boolean, isPull: Boolean, hasMore: Boolean, message: String? = "加载成功", stateData: MutableStateFlow<PullState> = _pullState){
        if(isRefresh){
            currentPage = 1
        }else{
            currentPage += 1
        }
        val result = stateData.value.copy()
        result.isRefresh = isRefresh
        result.isPull = isPull
        result.hasMore = hasMore
        result.loadingState = LoadingState.SUCCESS
        result.message = message
        result.code = ErrorStatus.SUCCESS
        stateData.value = result
    }

    fun pullError(isRefresh: Boolean, isPull: Boolean, code: Int, error: String, stateData: MutableStateFlow<PullState> = _pullState){
        val result = stateData.value.copy()
        result.isRefresh = isRefresh
        result.isPull = isPull
        result.hasMore = false
        result.loadingState = LoadingState.ERROR
        result.message = error
        result.code = code
        stateData.value = result
    }

    fun loadingErrorConsumer(): Consumer<Throwable>{
        return pullErrorConsumer(true, isPull = false)
    }

    /**
     * 带有下拉刷线|加载更多的回调，兼容errorConsumer()
     * @param isRefresh: 是否为刷新
     * @param isPull: 是否是下拉刷新or上拉加载更多
     * @see errorConsumer()
     * @return 异常捕获事件
     */
    fun pullErrorConsumer(isRefresh: Boolean, isPull: Boolean, stateData: MutableStateFlow<PullState> = _pullState): Consumer<Throwable> {
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
        viewModelScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                //lifecycleOwner生命周期 > STARTED, 开始消费事件
                loadingDialogChannel.collect { channel ->
                    val fragmentManager = when(lifecycleOwner){
                        is FragmentActivity ->  lifecycleOwner.supportFragmentManager
                        is Fragment -> lifecycleOwner.childFragmentManager
                        else -> null
                    }
                    fragmentManager ?: return@collect
                    when(channel){
                        is LoadingDialogEvent.LoadingDialogShowEvent -> {
                            api.showLoadingDialog(fragmentManager, lifecycleOwner, channel.job, channel.cancelEnable, channel.message)
                        }
                        is LoadingDialogEvent.LoadingDialogCloseEvent -> {
                            api.dismissLoadingDialog(fragmentManager)
                        }
                    }
                }
            }
        }
    }

    fun showLoadingDialog(disposable: Job? = null, cancelEnable: Boolean = true, message: String? = null){
        viewModelScope.launch {
            _loadingDialogChannel.send(LoadingDialogEvent.LoadingDialogShowEvent(disposable, cancelEnable, message))
        }
    }

    fun dismissLoadingDialog(){
        viewModelScope.launch {
            _loadingDialogChannel.send(LoadingDialogEvent.LoadingDialogCloseEvent)
        }
    }

}
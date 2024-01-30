package com.mellivora.base.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mellivora.base.utils.LogUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel: ViewModel() {


    // ---------------------------  协程处理 --------------------------------------
    fun doUILaunch(block: suspend CoroutineScope.(CoroutineContext) -> Unit): Job {
        return doUILaunch(block, null)
    }

    /**
     * 开启一个在UI线程运行的协程根作用域，使用该API创建的作用域，出异常时互不影响
     * @param block: 声明继承当前作用域(suspend CoroutineScope)，否则为一个普通函数
     * @param handler: 子协程｜当前作用域抛出的异常 (当前协程launch作用域将被关闭)
     */
    fun doUILaunch(block: suspend CoroutineScope.(CoroutineContext) -> Unit, handler: CoroutineExceptionHandler? = null): Job {
        val exceptionHandler = handler ?: CoroutineExceptionHandler { context, e ->
            e.printStackTrace()
            LogUtils.print2ConsoleError("doUILaunch():${e.stackTraceToString()}")
        }
        val job = viewModelScope.launch(Dispatchers.Main + exceptionHandler){
            block(this.coroutineContext)
        }
        return job
    }



    /**
     * 清理子任务
     */
    fun taskClear() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}
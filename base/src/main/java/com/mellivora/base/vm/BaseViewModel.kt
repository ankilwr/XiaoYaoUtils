package com.mellivora.base.vm

import androidx.core.util.Consumer
import androidx.lifecycle.ViewModel
import com.mellivora.base.bean.BaseData
import com.mellivora.base.exception.DataCheckException
import com.mellivora.base.exception.ErrorStatus
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel: ViewModel() {

    //使用SupervisorJob()，每个协程作用域自处理异常
    private val uiScope = CoroutineScope(SupervisorJob())

    // ---------------------------  协程处理 --------------------------------------
    fun doUILaunch(block: suspend CoroutineScope.(CoroutineContext) -> Unit): Job {
        return doUILaunch(block, null)
    }

    /**
     * 开启一个在UI线程运行的协程作用域，使用该API创建的作用域，出异常时互不影响
     * @param block: 声明继承当前作用域(suspend CoroutineScope)，否则为一个普通函数
     * @param onError: 子协程｜当前作用域抛出的异常 (当前协程launch作用域将被关闭)
     */
    fun doUILaunch(block: suspend CoroutineScope.(CoroutineContext) -> Unit, handler: CoroutineExceptionHandler? = null): Job {
        val exceptionHandler = handler ?: CoroutineExceptionHandler { context, e ->
            e.printStackTrace()
        }
        val job = uiScope.launch(Dispatchers.Main + exceptionHandler){
            block(this.coroutineContext)
        }
        return job
    }

    fun <T> CoroutineScope.doAsync(
        block: suspend CoroutineScope.() -> T
    ): Deferred<T> {
        return async(Dispatchers.IO, block = block)
    }


    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }


    /**
     * 清理子任务
     */
    fun taskClear() {
        uiScope.coroutineContext.cancelChildren()
    }

    //=================================  提供基础的 数据消费事件  ======================================
    //=================================  提供基础的 数据消费事件  ======================================
    //=================================  提供基础的 数据消费事件  ======================================
    /**
     * 验证服务端返回的数据是否正常, 验证code跟data, 默认只验证code
     * @param check: 是否要验证服务端返回的data变量(主要是验证data是否为空)
     * @param next：验证成功的回调事件，返回验证的Data数据
     * @return 服务端数据验证事件
     */
    fun <B> httpCheckConsumer(check: Boolean = false, next: (B?) -> Unit): Consumer<BaseData<B>> {
        return Consumer {
            //有些接口正常返回数据，但是不返回code的
            if (it.code != null && !it.httpCheck()) {
                throw DataCheckException(it.msg?:"", ErrorStatus.API_DATA_ERROR)
            }
            if(check){
                dataConsumer<B>{ data -> next.invoke(data) }.accept(it)
            }else{
                next(it.data)
            }
        }
    }

    /**
     * 只验证服务端返回的data数据是否正常，不验证code
     * @return 服务端数据验证数据
     */
    fun <B> dataConsumer(next: (B) -> Unit): Consumer<BaseData<B>> {
        return Consumer {
            if(it.data == null){
                throw DataCheckException(it.msg?:"", code = ErrorStatus.API_DATA_ERROR)
            }
            next(it.data)
        }
    }
}
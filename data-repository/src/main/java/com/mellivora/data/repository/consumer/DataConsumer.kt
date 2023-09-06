package com.mellivora.data.repository.consumer

import androidx.core.util.Consumer
import com.mellivora.base.exception.DataCheckException
import com.mellivora.base.exception.ErrorStatus
import com.mellivora.data.repository.bean.BaseData


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
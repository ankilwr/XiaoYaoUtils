package com.mellivora.base.coroutine

import androidx.core.util.Consumer
import androidx.lifecycle.MutableLiveData
import com.mellivora.base.exception.DataCheckException
import com.mellivora.base.exception.ErrorStatus
import com.mellivora.data.repository.bean.BaseData
import kotlinx.coroutines.Deferred


//=================================  提供基础的 数据消费事件  ======================================
//=================================  提供基础的 数据消费事件  ======================================
//=================================  提供基础的 数据消费事件  ======================================

/**
 * 校验BaseBean里的HttpCode
 * @return BaseBean.data的数据，校验失败则抛出DataCheckException
 */
@Throws(DataCheckException::class)
fun <T> BaseData<T>.httpCheckData(checkData: Boolean = false): T?{
    val it = this
    //有些接口正常返回数据，但是不返回code的,就跳过code验证
    if (it.code != null && "200" != it.code) {
        throw DataCheckException(it.msg ?: "", it.code)
    }
    //校验data
    return if(checkData){
        dataCheck()
    }else{
        it.data
    }
}

/**
 * 校验BaseBean里的data
 * @return BaseBean.data的数据，校验失败则抛出DataCheckException
 */
@Throws(DataCheckException::class)
fun <T> BaseData<T>.dataCheck(): T{
    val it = this
    if(it.data == null){
        throw DataCheckException(it.msg ?: "", code = "${ErrorStatus.API_DATA_ERROR}")
    }
    return it.data
}


/**
 * 等待数据返回&校验BaseBean里的HttpCode
 * @return BaseBean.data的数据，校验失败则返回null
 */
suspend fun <T : Any> Deferred<IResult<BaseData<T>>>.awaitBaseDataForHttpOrNull(checkData: Boolean = false): T?{
    return try{
        await().getData().httpCheckData(checkData)
    }catch (e: Throwable){
        null
    }
}

/**
 * 等待数据返回&校验BaseBean里的Data数据
 * @return BaseBean.data的数据，校验失败则返回null
 */
suspend fun <T : Any> Deferred<IResult<BaseData<T>>>.awaitBaseDataOrNull(): T?{
    return try{
        await().getData().dataCheck()
    }catch (e: Throwable){
        null
    }
}

/**
 * 等待数据返回&校验BaseBean里的HttpCode
 * @return BaseBean.data的数据，校验失败则抛出DataCheckException
 */
@Throws(DataCheckException::class)
suspend fun <T : Any> Deferred<IResult<BaseData<T>>>.awaitBaseDataForHttp(checkData: Boolean = false): T?{
    val it = await().getData()
    return it.httpCheckData(checkData)
}

/**
 * 等待数据返回&校验BaseBean里的Data数据
 * @return BaseBean.data的数据，校验失败则抛出DataCheckException
 */
@Throws(DataCheckException::class)
suspend fun <T : Any> Deferred<IResult<BaseData<T>>>.awaitBaseData(): T{
    val it = await().getData()
    return it.dataCheck()
}

/**
 * 验证服务端返回的数据是否正常, 验证code跟data, 默认只验证code
 * @param checkData: 是否要验证服务端返回的data变量(主要是验证data是否为空)
 * @param next：验证成功的回调事件，返回验证的Data数据
 * @return 服务端数据验证事件
 */
fun <B> httpCheckConsumer(checkData: Boolean = false, next: (B?) -> Unit): Consumer<BaseData<B>> {
    return Consumer {
        val data = it.httpCheckData(checkData)
        next(data)
    }
}

/**
 * 只验证服务端返回的data数据是否正常，不验证code
 * @return 服务端数据验证数据
 */
fun <B> dataConsumer(next: (B) -> Unit): Consumer<BaseData<B>> {
    return Consumer {
        next(it.dataCheck())
    }
}




//=========================== 列表数据更新 ===========================
/**
 * 更新列表数据
 * @param itemList: 需要更新的数据源列表
 * @param loadList: 接口｜数据库获取的数据
 */
inline fun <reified T> updateUiList(isRefresh: Boolean, itemList: MutableLiveData<MutableList<T>>, loadList: List<T>?){
    if(isRefresh){
        itemList.value?.clear()
    }
    val list = itemList.value ?: mutableListOf()
    loadList?.let {
        list.addAll(loadList)
    }
    itemList.value = list
}


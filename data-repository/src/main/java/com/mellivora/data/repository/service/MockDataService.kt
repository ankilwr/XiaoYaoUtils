package com.mellivora.data.repository.service

import android.content.Context
import com.mellivora.base.coroutine.safeResume
import com.mellivora.base.coroutine.safeResumeWithException
import com.mellivora.base.expansion.jsonConvertBeanThrow
import com.mellivora.data.repository.bean.BaseData
import com.mellivora.data.repository.bean.CommunityData
import kotlinx.coroutines.suspendCancellableCoroutine


/**
 * 模拟数据服务
 */
interface MockDataService {

    /**
     * 模拟加载朋友圈列表数据
     */
    suspend fun getCommunityData(context: Context, page: Int, size: Int): BaseData<List<CommunityData>> = suspendCancellableCoroutine { coroutine ->
        try {
            val baseData = context.assets.open("community.json").bufferedReader().use {
                it.readText()
            }.jsonConvertBeanThrow<BaseData<List<CommunityData>>>()
            Thread.sleep(1000L)
            coroutine.safeResume(baseData)
        }catch (e: Throwable){
            coroutine.safeResumeWithException(e)
        }
    }
}
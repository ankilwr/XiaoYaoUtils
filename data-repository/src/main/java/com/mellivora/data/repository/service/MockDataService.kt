package com.mellivora.data.repository.service

import android.content.Context
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
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

    /**
     * 模拟发表评论(具体参数后续看接口去调整)
     * @param communityDataId: 朋友圈的动态ID
     * @param discuss: 朋友圈的动态下的某条评论(用来模拟结果的返回，实际接口可能只需要一个ID即可)
     * @param editContent:【评论｜回复】的内容
     * @return 评论发布结果
     */
    suspend fun postDiscuss(
        communityDataId: String?,
        discuss: CommunityData.Discuss?,
        editContent: String?
    ): BaseData<CommunityData.Discuss> = suspendCancellableCoroutine { coroutine ->
        val mockResult = JsonObject()
        mockResult.addProperty("code", 200)
        mockResult.addProperty("msg", "success")
        val dataJson = JsonObject().apply {
            addProperty("sender", "逍遥")
            addProperty("sender_id", "333")
            addProperty("content", editContent)
            //回复的具体用户
            addProperty("recipient", discuss?.sender)
            addProperty("recipient_id", discuss?.senderId)
        }
        mockResult.add("data", dataJson)

        val responseData = mockResult.toString().jsonConvertBeanThrow<BaseData<CommunityData.Discuss>>()
        Thread.sleep(1000L)
        coroutine.safeResume(responseData)
    }
}
package com.shihang.kotlin.vm

import androidx.lifecycle.MutableLiveData
import com.mellivora.data.repository.consumer.httpCheckConsumer
import com.mellivora.data.repository.consumer.httpCheckData
import com.mellivora.base.coroutine.withIOResult
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.data.repository.consumer.updateUiList
import com.mellivora.base.expansion.showToast
import com.mellivora.base.utils.Utils
import com.mellivora.base.vm.LoadingViewModel
import com.mellivora.data.repository.bean.CommunityData
import com.mellivora.data.repository.service.BaseService

/**
 * 朋友圈VM
 */
class CommunityListViewModel: LoadingViewModel(){

    val dataList = MutableLiveData<MutableList<CommunityData>>()

    //编辑模式下的临时内容
    val communityData = MutableLiveData<CommunityData?>()
    val discussData = MutableLiveData<CommunityData.Discuss?>()
    val editContent = MutableLiveData<CharSequence>()

    fun loadListData(isRefresh: Boolean, isPull: Boolean){
        doUILaunch {
            loading(isRefresh, isPull)
            withIOResult {
                val context = Utils.getApp()
                val loadPage = getLoadPage(isRefresh)
                BaseService.mockService.getCommunityData(context, loadPage, defaultPageSize)
            }.onCheckSuccess {
                val listData = it.httpCheckData()
                updateUiList(isRefresh, dataList, listData)
                pullSuccess(isRefresh, isPull, it.hasMore())
            }.onCheckError(pullErrorConsumer(isRefresh, isPull))
        }
    }

    /**
     * 切换为编辑评论模式
     * @param data: 朋友圈的某条动态
     * @param discuss: 朋友圈的某条动态下的评论(回复某某时传)
     */
    fun showDiscussEditMode(data: CommunityData, discuss: CommunityData.Discuss? = null){
        communityData.value = data
        discussData.value = discuss
    }

    /**
     * 清除编辑模式产生的临时数据
     */
    fun clearDiscussEditMode(){
        communityData.value = null
        discussData.value = null
        editContent.value = ""
    }

    /**
     * 发表评论
     */
    fun postDiscuss(){
        val editCommunity = communityData.value
        val editText = editContent.value?.toString()
        if(editText.isNullOrEmpty()){
            showToast("请输入评论内容")
            return
        }
        if(editCommunity == null){
            showToast("朋友圈动态ID错误")
            return
        }
        val job = doUILaunch {
            withIOResult {
                BaseService.mockService.postDiscuss(editCommunity.id, discussData.value, editText)
            }.onCheckSuccess(httpCheckConsumer(true){ newDiscuss ->
                dismissLoadingDialog()
                val newList = mutableListOf<CommunityData>()
                dataList.value?.let { newList.addAll(it) }
                newList.forEachIndexed { index, it ->
                    if(it.id == editCommunity.id){
                        val copyData = it.copy()
                        val discussList = copyData.discuss ?: mutableListOf()
                        discussList.add(newDiscuss!!)
                        copyData.discuss = discussList
                        newList[index] = copyData
                        dataList.value = newList
                        clearDiscussEditMode()
                        return@httpCheckConsumer
                    }
                }
            }).onCheckError(errorConsumer())
        }
        showLoadingDialog(job)
    }

}
package com.shihang.kotlin.vm

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.mellivora.base.coroutine.httpCheckData
import com.mellivora.base.coroutine.withIOResult
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.base.coroutine.updateUiList
import com.mellivora.base.expansion.childFragmentManager
import com.mellivora.base.utils.Utils
import com.mellivora.base.vm.LoadingViewModel
import com.mellivora.data.repository.bean.CommunityData
import com.mellivora.data.repository.service.BaseService
import com.shihang.kotlin.ui.dialog.DiscussEditDialog

/**
 * 朋友圈VM
 */
class CommunityListViewModel: LoadingViewModel(){

    val dataList = MutableLiveData<MutableList<CommunityData>>()

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
    fun showDiscussEditMode(v: View, data: CommunityData, discuss: CommunityData.Discuss? = null){
        val fragmentManager = v.childFragmentManager ?: return
        val dialog = DiscussEditDialog.getInstance(data.id, discuss)
        dialog.showNow(fragmentManager){ _, resultBundle ->
            //resultBundle结果，可查看DiscussEditDialog::dismissForResult的传参
            val id = resultBundle.getString("communityDataId")
            val newDiscuss: CommunityData.Discuss? = resultBundle.getParcelable("newDiscuss")
            newDiscuss ?: return@showNow
            //更新列表UI
            val newList = mutableListOf<CommunityData>()
            dataList.value?.let { newList.addAll(it) }
            newList.forEachIndexed { index, it ->
                if(it.id == id){
                    val copyData = it.copy()
                    val discussList = copyData.discuss ?: mutableListOf()
                    discussList.add(newDiscuss)
                    copyData.discuss = discussList
                    newList[index] = copyData
                    dataList.value = newList
                    return@showNow
                }
            }
        }
    }

}
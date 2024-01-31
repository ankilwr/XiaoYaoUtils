package com.shihang.kotlin.vm

import androidx.lifecycle.MutableLiveData
import com.mellivora.base.coroutine.httpCheckData
import com.mellivora.base.coroutine.withIOResult
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.base.coroutine.updateUiList
import com.mellivora.base.utils.Utils
import com.mellivora.base.vm.LoadingViewModel
import com.mellivora.data.repository.bean.CommunityData
import com.mellivora.data.repository.service.BaseService

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

}
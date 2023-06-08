package com.shihang.kotlin.vm

import androidx.lifecycle.MutableLiveData
import com.mellivora.base.coroutine.doIOResult
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.base.http.executeConvert
import com.mellivora.base.repository.BaseService
import com.mellivora.base.vm.LoadingViewModel
import com.shihang.kotlin.bean.GithubRepositoryBean


class RepositoryListViewModel: LoadingViewModel(){

    val dataList = MutableLiveData<MutableList<GithubRepositoryBean>>()

    fun loadListData(isRefresh: Boolean, isPullAction: Boolean){
        doUILaunch {
            if(isRefresh && !isPullAction){
                //将UI视图更新为loading状态
                loading()
            }
            doIOResult {
                val call = BaseService.githubService.getGithubRepositoryList("ankilwr")
                call.executeConvert<MutableList<GithubRepositoryBean>>()
            }.onCheckSuccess {
                dataList.value = it
                pullSuccess(isRefresh, isPullAction, false)
            }.onCheckError(pullErrorConsumer(isRefresh, isPullAction))
        }
    }

}
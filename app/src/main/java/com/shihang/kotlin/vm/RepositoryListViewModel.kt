package com.shihang.kotlin.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import com.mellivora.base.coroutine.doIOResult
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.data.repository.http.executeConvert
import com.mellivora.base.vm.LoadingViewModel
import com.mellivora.data.repository.service.BaseService
import com.shihang.kotlin.bean.GithubRepositoryBean


class RepositoryListViewModel: LoadingViewModel(){

    val dataList = MutableLiveData<MutableList<GithubRepositoryBean>>()
    val dataStateList = mutableStateListOf<GithubRepositoryBean>()

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
                val newList = mutableListOf<GithubRepositoryBean>()
                if(!isRefresh){
                    dataList.value?.let { oldList -> newList.addAll(oldList) }
                }
                newList.addAll(it)
                dataList.value = newList
                dataStateList.clear()
                dataStateList.addAll(newList)
                pullSuccess(isRefresh, isPullAction, false)
                println(System.currentTimeMillis())
            }.onCheckError(pullErrorConsumer(isRefresh, isPullAction))
        }
    }

}
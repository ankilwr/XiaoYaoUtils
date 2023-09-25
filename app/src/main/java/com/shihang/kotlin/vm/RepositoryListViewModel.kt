package com.shihang.kotlin.vm

import androidx.lifecycle.MutableLiveData
import com.mellivora.base.coroutine.doIOResult
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.data.repository.http.executeConvert
import com.mellivora.base.vm.LoadingViewModel
import com.mellivora.data.repository.service.BaseService
import com.shihang.kotlin.bean.GithubRepositoryBean
import kotlinx.coroutines.delay


class RepositoryListViewModel: LoadingViewModel(){

    val dataList = MutableLiveData<List<GithubRepositoryBean>>()

    fun loadListData(isRefresh: Boolean, isPull: Boolean){
        doUILaunch {
            loading(isRefresh, isPull)
            doIOResult {
                delay(2000L)
                val call = BaseService.githubService.getGithubRepositoryList("ankilwr")
                call.executeConvert<MutableList<GithubRepositoryBean>>()
            }.onCheckSuccess {
                val newList = mutableListOf<GithubRepositoryBean>()
                if(!isRefresh){
                    dataList.value?.let { oldList -> newList.addAll(oldList) }
                }
                newList.addAll(it)
                dataList.value = newList
                pullSuccess(isRefresh, isPull, false)
            }.onCheckError(pullErrorConsumer(isRefresh, isPull))
        }
    }

    fun removePosition(p: Int){
        //loadListData(false, isPull = true)
    }

}
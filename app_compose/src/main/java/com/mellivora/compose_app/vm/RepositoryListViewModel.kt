package com.mellivora.compose_app.vm

import androidx.compose.runtime.mutableStateListOf
import com.mellivora.base.coroutine.withIOResult
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.data.repository.http.executeConvert
import com.mellivora.base.vm.LoadingViewModel
import com.mellivora.compose_app.bean.GithubRepositoryBean
import com.mellivora.data.repository.service.BaseService
import kotlinx.coroutines.delay


class RepositoryListViewModel: LoadingViewModel(){

    val dataStateList = mutableStateListOf<GithubRepositoryBean>()

    fun loadListData(isRefresh: Boolean, isPull: Boolean){
        doUILaunch {
            loading(isRefresh, isPull)
            withIOResult {
                delay(2000L)
                val call = BaseService.githubService.getGithubRepositoryList("ankilwr")
                call.executeConvert<MutableList<GithubRepositoryBean>>()
            }.onCheckSuccess {
                if(isRefresh){
                    dataStateList.clear()
                }
                dataStateList.addAll(it)
                pullSuccess(isRefresh, isPull, false)
            }.onCheckError(pullErrorConsumer(isRefresh, isPull))
        }
    }

    fun removePosition(p: Int){
        //loadListData(false, isPull = true)
        dataStateList.removeAt(p)
    }

}
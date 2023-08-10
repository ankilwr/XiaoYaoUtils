package com.shihang.kotlin.ui.activity

import android.util.Log
import androidx.activity.viewModels
import com.mellivora.base.coroutine.doIOResult
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.base.expansion.createIntent
import com.mellivora.base.expansion.setMultipleClick
import com.mellivora.base.expansion.showToast
import com.mellivora.base.http.executeConvert
import com.mellivora.base.repository.BaseService
import com.mellivora.base.ui.activity.BaseBindingActivity
import com.mellivora.base.vm.LoadingViewModel
import com.shihang.kotlin.bean.GithubUserBean
import com.shihang.kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.delay

class MainActivity: BaseBindingActivity<ActivityMainBinding>(){

    private val viewModel: MainVm by viewModels()

    override fun initBinding(binding: ActivityMainBinding) {
        viewModel.registerLoadingDialog(this, this)
    }

    override fun initViews() {
        viewBinding.btnListDemo.setMultipleClick {
            val intent = createIntent(RepositoryListActivity::class.java)
            it.context.startActivity(intent)
        }
        viewBinding.btnPermissionDemo.setMultipleClick {
            val intent = createIntent(PermissionActivity::class.java)
            it.context.startActivity(intent)
        }
        viewBinding.btnCompose.setMultipleClick {
            val intent = createIntent(ComposeActivity::class.java)
            it.context.startActivity(intent)
        }
        viewBinding.btnUserInfo.setMultipleClick {
            viewModel.loadGithubUserInfo()
        }
    }

    class MainVm: LoadingViewModel(){

        fun loadGithubUserInfo(){
            val job = doUILaunch{
                doIOResult {
                    val call = BaseService.githubService.getGithubUserInfo("ankilwr")
                    call.executeConvert<GithubUserBean>()
                }.onCheckSuccess {
                    Log.i("测试测试", "onCheckSuccess")
                    dismissLoadingDialog()
                    showToast(it.toString())
                }.onCheckError(errorConsumer{
                    Log.i("测试测试", "onCheckError:$it")
                })
            }
            showLoadingDialog(job)
        }
    }

}

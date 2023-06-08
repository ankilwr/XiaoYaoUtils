package com.shihang.kotlin.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import com.mellivora.base.adapter.BaseMultiTypeAdapter
import com.mellivora.base.adapter.BindingItemViewBinder
import com.mellivora.base.ui.activity.BaseBindingActivity
import com.mmc.almanac.adapter.RecyclerHolder
import com.shihang.kotlin.bean.GithubRepositoryBean
import com.shihang.kotlin.databinding.ActivityRepositoryListBinding
import com.shihang.kotlin.databinding.ItemRepositoryListBinding
import com.shihang.kotlin.vm.RepositoryListViewModel

/**
 * 列表示例
 */
class RepositoryListActivity: BaseBindingActivity<ActivityRepositoryListBinding>(){

    private val viewModel: RepositoryListViewModel by viewModels()

    override fun initBinding(binding: ActivityRepositoryListBinding) {
        //viewModel.registerLoadingDialog(this, this)
        binding.vm = viewModel
        binding.adapter = BaseMultiTypeAdapter().apply {
            register(RepositoryBinder())
        }
    }

    override fun initViews() {
        viewBinding.appThemeBar.setLeftIconClick{
            finish()
        }
        viewModel.loadListData(isRefresh = true, isPullAction = false)
    }



    private inner class RepositoryBinder: BindingItemViewBinder<GithubRepositoryBean, ItemRepositoryListBinding>(){
        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemRepositoryListBinding {
            return ItemRepositoryListBinding.inflate(inflater, parent, false)
        }
        override fun onBindViewHolder(binding: ItemRepositoryListBinding, data: GithubRepositoryBean, holder: RecyclerHolder) {
            binding.data = data
        }
    }

}

package com.shihang.kotlin.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import com.mellivora.base.binding.adapter.BaseMultiTypeAdapter
import com.mellivora.base.binding.adapter.BindingItemViewBinder
import com.mellivora.base.binding.adapter.RecyclerHolder
import com.mellivora.base.binding.ui.activity.BaseBindingActivity
import com.mellivora.base.expansion.setMultipleClick
import com.mellivora.data.repository.bean.CommunityData
import com.shihang.kotlin.R
import com.shihang.kotlin.databinding.ActivityCommunityListBinding
import com.shihang.kotlin.databinding.ItemCommunityImageBinding
import com.shihang.kotlin.databinding.ItemCommunityImageChildBinding
import com.shihang.kotlin.databinding.ItemCommunityLinkBinding
import com.shihang.kotlin.databinding.ItemCommunityNormalBinding
import com.shihang.kotlin.databinding.ItemCommunityVideoBinding
import com.shihang.kotlin.vm.CommunityListViewModel


/**
 * 朋友圈Activity
 */
class CommunityActivity: BaseBindingActivity<ActivityCommunityListBinding>() {

    private val viewModel: CommunityListViewModel by viewModels()

    override fun initBinding(binding: ActivityCommunityListBinding) {
        binding.adapter = BaseMultiTypeAdapter().apply {
            //注册一对多类型的绑定(一种数据类型对应多种布局)
            register(CommunityData::class).to(
                NormalBinder(),
                LinkBinder(),
                VideoBinder(),
                ImageBinder()
            ).withKotlinClassLinker{ _, data ->
                //根据数据里的type返回对应的Binder解析
                when(data.type){
                    "link" -> LinkBinder::class
                    "image" -> ImageBinder::class
                    "video" -> VideoBinder::class
                    else -> NormalBinder::class
                }
            }
        }
        binding.vm = viewModel
    }

    override fun initViews() {
        viewModel.loadListData(true, isPull = false)
    }

    /**
     * 普通类型的朋友圈
     */
    class NormalBinder: BindingItemViewBinder<CommunityData, ItemCommunityNormalBinding>(){
        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemCommunityNormalBinding {
            return ItemCommunityNormalBinding.inflate(inflater, parent, false)
        }
        override fun onBindViewHolder(binding: ItemCommunityNormalBinding, data: CommunityData, holder: RecyclerHolder) {
            binding.data = data
        }
    }

    /**
     * 视频类型的朋友圈
     */
    class VideoBinder: BindingItemViewBinder<CommunityData, ItemCommunityNormalBinding>(){
        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemCommunityNormalBinding {
            return ItemCommunityNormalBinding.inflate(inflater, parent, false).apply {
                val videoView = LayoutInflater.from(parent.context).inflate(R.layout.item_community_video, parent, false)
                typeContainer.addView(videoView)
                typeContainer.visibility = View.VISIBLE
            }
        }
        override fun onBindViewHolder(binding: ItemCommunityNormalBinding, data: CommunityData, holder: RecyclerHolder) {
            //绑定根Item数据
            binding.data = data
            //处理视屏子视图
            val videoContainer = binding.typeContainer.findViewById<View>(R.id.videoContainer)
            val videoBinding = ItemCommunityVideoBinding.bind(videoContainer)
            videoBinding.data = data.video
        }
    }

    /**
     * 图片类型的朋友圈
     */
    class ImageBinder: BindingItemViewBinder<CommunityData, ItemCommunityNormalBinding>(){
        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemCommunityNormalBinding {
            return ItemCommunityNormalBinding.inflate(inflater, parent, false).apply {
                val linkView = LayoutInflater.from(parent.context).inflate(R.layout.item_community_image, parent, false)
                typeContainer.addView(linkView)
                typeContainer.visibility = View.VISIBLE
            }
        }
        override fun onBindViewHolder(binding: ItemCommunityNormalBinding, data: CommunityData, holder: RecyclerHolder) {
            //绑定根Item数据
            binding.data = data
            //处理图片子视图
            val imagesContainer = binding.typeContainer.findViewById<View>(R.id.imagesContainer)
            val imagesBinding = ItemCommunityImageBinding.bind(imagesContainer)
            if(imagesBinding.adapter == null){
                imagesBinding.adapter = BaseMultiTypeAdapter().apply {
                    register(ImageSingleBinder())
                }
            }
            imagesBinding.data = data
        }
    }

    /**
     * 单张图片绑定
     */
    class ImageSingleBinder: BindingItemViewBinder<CommunityData.Image, ItemCommunityImageChildBinding>(){
        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemCommunityImageChildBinding {
            return ItemCommunityImageChildBinding.inflate(inflater, parent, false)
        }
        override fun onBindViewHolder(binding: ItemCommunityImageChildBinding, data: CommunityData.Image, holder: RecyclerHolder) {
            binding.data = data
        }
    }

    /**
     * 分享链接类型的朋友圈
     */
    class LinkBinder: BindingItemViewBinder<CommunityData, ItemCommunityNormalBinding>(){
        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemCommunityNormalBinding {
            return ItemCommunityNormalBinding.inflate(inflater, parent, false).apply {
                val linkView = LayoutInflater.from(parent.context).inflate(R.layout.item_community_link, parent, false)
                typeContainer.addView(linkView)
                typeContainer.visibility = View.VISIBLE
            }
        }
        override fun onBindViewHolder(binding: ItemCommunityNormalBinding, data: CommunityData, holder: RecyclerHolder) {
            //绑定根Item数据
            binding.data = data
            //处理link子视图
            val linkContainer = binding.typeContainer.findViewById<View>(R.id.linkContainer)
            val linkBinding = ItemCommunityLinkBinding.bind(linkContainer)
            linkBinding.data = data.link
            linkBinding.root.setMultipleClick {
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(data.link?.url))
                it.context.startActivity(intent)
            }
        }
    }

}
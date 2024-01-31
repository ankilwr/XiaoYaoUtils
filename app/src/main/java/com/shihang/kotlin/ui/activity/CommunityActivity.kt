package com.shihang.kotlin.ui.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.mellivora.base.api.textview.ClickMovementMethod
import com.mellivora.base.binding.adapter.BaseMultiTypeAdapter
import com.mellivora.base.binding.adapter.BindingItemViewBinder
import com.mellivora.base.binding.adapter.RecyclerHolder
import com.mellivora.base.binding.ui.activity.BaseBindingActivity
import com.mellivora.base.expansion.getClickText
import com.mellivora.base.expansion.getColorText
import com.mellivora.base.expansion.setMultipleClick
import com.mellivora.base.expansion.showToast
import com.mellivora.data.repository.bean.CommunityData
import com.shihang.kotlin.R
import com.shihang.kotlin.databinding.ActivityCommunityListBinding
import com.shihang.kotlin.databinding.ItemCommunityDiscussBinding
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
        binding.vm = viewModel
        binding.adapter = BaseMultiTypeAdapter().apply {
            //回复某某评论的点击事件
            val discussClick = fun (data:CommunityData, discuss: CommunityData.Discuss){

            }
            //注册一对多类型的绑定(一种数据类型对应多种布局)
            register(CommunityData::class).to(
                NormalBinder(discussClick),
                LinkBinder(discussClick),
                VideoBinder(discussClick),
                ImageBinder(discussClick)
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
    }

    override fun initViews() {
        viewModel.loadListData(true, isPull = false)
    }

    /**
     * 普通类型的朋友圈(基础文字)
     * @param onDiscussClick: 去回复xxx
     */
    open class NormalBinder(
        private val onDiscussClick:(data:CommunityData, discuss: CommunityData.Discuss)->Unit
    ): BindingItemViewBinder<CommunityData, ItemCommunityNormalBinding>(){
        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemCommunityNormalBinding {
            return ItemCommunityNormalBinding.inflate(inflater, parent, false).apply {
                tvUp.highlightColor = Color.TRANSPARENT
                tvUp.movementMethod = ClickMovementMethod()
                discussAdapter = BaseMultiTypeAdapter().apply {
                    register(DiscussBinder())
                }
            }
        }
        override fun onBindViewHolder(binding: ItemCommunityNormalBinding, data: CommunityData, holder: RecyclerHolder) {
            binding.data = data
            val upText = SpannableStringBuilder()
            data.up?.forEach { upUser->
                if(upText.isEmpty()){
                    upText.append("♡ ")
                }else{
                    upText.append("、")
                }
                val clickSpan = getClickText(upUser.nickname){
                    showToast("你点击了${upUser.nickname}")
                }
                upText.append(clickSpan)
            }
            binding.tvUp.text = upText
            bindTypeContainerHolder(data, holder)

            binding.discussAdapter?.onChildItemClick = { discuss ->
                discuss as CommunityData.Discuss
                onDiscussClick.invoke(data, discuss)
            }
        }
        /**
         * 添加子视图的ViewBinding
         */
        fun attachTypeContainerBinding(holder: RecyclerHolder, viewId: Int, childBinding: ViewBinding){
            holder.getBinding<ItemCommunityNormalBinding>().apply {
                holder.saveChildBinding(viewId, childBinding)
                typeContainer.addView(childBinding.root)
                typeContainer.visibility = View.VISIBLE
            }
        }

        /**
         * 绑定不同类型的扩展
         */
        open fun bindTypeContainerHolder(data: CommunityData, holder: RecyclerHolder){
        }
    }

    /**
     * 分享链接类型的朋友圈
     */
    class LinkBinder(
        onDiscussClick:(data:CommunityData, discuss: CommunityData.Discuss)->Unit
    ): NormalBinder(onDiscussClick){
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerHolder {
            val holder = super.onCreateViewHolder(inflater, parent)
            val linkBinding = ItemCommunityLinkBinding.inflate(inflater, parent, false)
            attachTypeContainerBinding(holder, R.id.linkContainer, linkBinding)
            return holder
        }

        override fun bindTypeContainerHolder(data: CommunityData, holder: RecyclerHolder) {
            val linkBinding = holder.getChildBinding<ItemCommunityLinkBinding>(R.id.linkContainer) ?: return
            //处理link子视图
            linkBinding.data = data.link
            linkBinding.root.setMultipleClick {
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(data.link?.url))
                it.context.startActivity(intent)
            }
        }
    }

    /**
     * 视频类型的朋友圈
     */
    class VideoBinder(
        onDiscussClick:(data:CommunityData, discuss: CommunityData.Discuss)->Unit
    ): NormalBinder(onDiscussClick){
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerHolder {
            val holder = super.onCreateViewHolder(inflater, parent)
            val videoBinding = ItemCommunityVideoBinding.inflate(inflater, parent, false)
            attachTypeContainerBinding(holder, R.id.videoContainer, videoBinding)
            return holder
        }

        override fun bindTypeContainerHolder(data: CommunityData, holder: RecyclerHolder) {
            //处理视屏子视图
            val videoBinding = holder.getChildBinding<ItemCommunityVideoBinding>(R.id.videoContainer) ?: return
            videoBinding.data = data.video
        }
    }

    /**
     * 图片类型的朋友圈
     */
    class ImageBinder(
        onDiscussClick:(data:CommunityData, discuss: CommunityData.Discuss)->Unit
    ): NormalBinder(onDiscussClick){
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerHolder {
            val holder = super.onCreateViewHolder(inflater, parent)
            val imagesBinding = ItemCommunityImageBinding.inflate(inflater, parent, false).apply {
                adapter = BaseMultiTypeAdapter().apply {
                    register(ImageSingleBinder())
                }
            }
            attachTypeContainerBinding(holder, R.id.imagesContainer, imagesBinding)
            return holder
        }
        override fun bindTypeContainerHolder(data: CommunityData, holder: RecyclerHolder) {
            //处理图片子视图
            val imagesBinding = holder.getChildBinding<ItemCommunityImageBinding>(R.id.imagesContainer) ?: return
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
     * 朋友圈评论
     */
    class DiscussBinder: BindingItemViewBinder<CommunityData.Discuss, ItemCommunityDiscussBinding>(){
        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemCommunityDiscussBinding {
            return ItemCommunityDiscussBinding.inflate(inflater, parent, false)
        }
        override fun onBindViewHolder(binding: ItemCommunityDiscussBinding, data: CommunityData.Discuss, holder: RecyclerHolder) {
            val color = Color.parseColor("#5b7fe4")
            val builder = SpannableStringBuilder()
            builder.append(getColorText(data.sender, color)?:"")
            if(!data.recipientId.isNullOrEmpty()){
                builder.append("回复")
                builder.append(getColorText(data.recipient, color)?:"")
            }
            builder.append(": ${data.content}")

            binding.tvDiscuss.text = builder
        }
    }

}
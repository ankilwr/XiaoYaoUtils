package com.shihang.kotlin.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.mellivora.base.R
import com.mellivora.base.api.getDefaultLoadingDialogApi
import com.mellivora.base.binding.ui.dialog.BaseBindingDialog
import com.mellivora.base.coroutine.httpCheckConsumer
import com.mellivora.base.coroutine.onCheckError
import com.mellivora.base.coroutine.onCheckSuccess
import com.mellivora.base.coroutine.withIOResult
import com.mellivora.base.expansion.setMultipleClick
import com.mellivora.base.expansion.showSoftInputFromWindow
import com.mellivora.base.expansion.showToast
import com.mellivora.base.vm.LoadingViewModel
import com.mellivora.data.repository.bean.CommunityData
import com.mellivora.data.repository.service.BaseService
import com.shihang.kotlin.databinding.DialogCommunityDiscussEditBinding

/**
 * 朋友圈评论编辑
 */
class DiscussEditDialog: BaseBindingDialog<DialogCommunityDiscussEditBinding>() {

    private val viewModel: DiscussEditViewModel by viewModels()

    companion object{
        fun getInstance(
            communityDataId: String?, //需要回复朋友圈动态ID
            discuss: CommunityData.Discuss? //需要回复的某条评论
        ): DiscussEditDialog{
            val dialog = DiscussEditDialog()
            dialog.arguments = Bundle().apply {
                putString("communityDataId", communityDataId)
                putParcelable("discuss", discuss)
            }
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), R.style.Dialog_Bottom).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.apply {
                decorView.setPadding(0, 0, 0, 0)
                attributes?.let {
                    it.width = WindowManager.LayoutParams.MATCH_PARENT
                    it.height = WindowManager.LayoutParams.WRAP_CONTENT
                    it.gravity = Gravity.BOTTOM
                    window!!.attributes = it
                }
                setDimAmount(0f)
            }
        }
    }

    override fun initViews(binding: DialogCommunityDiscussEditBinding) {
        viewModel.registerLoadingDialog(this, getDefaultLoadingDialogApi())
        val communityDataId = arguments?.getString("communityDataId")
        val discuss: CommunityData.Discuss? = arguments?.getParcelable("discuss")

        binding.etReply.showSoftInputFromWindow()
        binding.etReply.hint = discuss?.let { "回复\"${it.sender}\"" } ?: "评论"
        binding.btnReply.setMultipleClick {
            val editContent = binding.etReply.text.toString()
            //发表评论
            viewModel.postDiscuss(communityDataId, discuss, editContent){
                //发表成功，回调发表结果并结束评论编辑弹窗
                val resultBundle = Bundle()
                resultBundle.putString("communityDataId", communityDataId)
                resultBundle.putParcelable("newDiscuss", it)
                dismissForResult(resultBundle)
            }
        }
    }

    class DiscussEditViewModel: LoadingViewModel(){
        /**
         * 发表评论
         * @param communityDataId: 朋友圈的动态ID
         * @param discuss: 朋友圈的动态下的某条评论
         * @param editContent:【评论｜回复】的内容
         */
        fun postDiscuss(
            communityDataId: String?,
            discuss: CommunityData.Discuss?,
            editContent: String?,
            onPostResult:(CommunityData.Discuss) -> Unit
        ){
            if(editContent.isNullOrEmpty()){
                showToast("请输入评论内容")
                return
            }
            if(communityDataId.isNullOrEmpty()){
                showToast("朋友圈动态ID错误")
                return
            }
            val job = doUILaunch {
                withIOResult {
                    BaseService.mockService.postDiscuss(communityDataId, discuss, editContent)
                }.onCheckSuccess(httpCheckConsumer(true){
                    onPostResult.invoke(it!!)
                }).onCheckError(errorConsumer())
            }
            showLoadingDialog(job)
        }
    }
}
package com.mellivora.base.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.core.view.isGone
import com.mellivora.base.R
import com.mellivora.base.databinding.BaseDialogLoadingBinding


class LoadingDialog: BaseBindingDialog<BaseDialogLoadingBinding>() {

    private var loadingMessage: CharSequence? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), R.style.Dialog_Loading).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.decorView?.setPadding(0, 0, 0, 0)
            window?.attributes?.let {
                it.width = WindowManager.LayoutParams.WRAP_CONTENT
                it.height = WindowManager.LayoutParams.WRAP_CONTENT
                it.gravity = Gravity.CENTER
                window!!.attributes = it
            }
            setCanceledOnTouchOutside(false)
        }
    }

    override fun initViews(binding: BaseDialogLoadingBinding) {
        setMessage(loadingMessage)
    }

    fun setMessage(message: CharSequence?): LoadingDialog{
        this.loadingMessage = message
        viewBinding?.let {
            it.tvMessage.text = message
            it.tvMessage.isGone = message.isNullOrEmpty()
        }
        return this
    }

}
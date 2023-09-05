package com.mellivora.base.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.view.isGone
import com.mellivora.base.R
import com.mellivora.base.databinding.BaseDialogLoadingBinding


class LoadingDialog: BaseCoreDialog() {

    private var loadingMessage: CharSequence? = null
    private var binding: BaseDialogLoadingBinding? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BaseDialogLoadingBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMessage(loadingMessage)
    }

    fun setMessage(message: CharSequence?): LoadingDialog{
        this.loadingMessage = message
        binding?.let {
            it.tvMessage.text = message
            it.tvMessage.isGone = message.isNullOrEmpty()
        }
        return this
    }

}
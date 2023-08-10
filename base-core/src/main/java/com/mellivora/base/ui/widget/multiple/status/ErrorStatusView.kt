package com.mellivora.base.ui.widget.multiple.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mellivora.base.databinding.BaseLayoutStatusErrorBinding
import com.mellivora.base.ui.widget.multiple.MultipleStatusView
import com.mellivora.base.ui.widget.multiple.api.MultipleStatus

/**
 * 错误试图
 */
class ErrorStatusView : MultipleStatus {

    private var viewBinding: BaseLayoutStatusErrorBinding? = null

    override fun onCreateView(context: Context, statusView: MultipleStatusView): View {
        if (viewBinding == null) {
            viewBinding = BaseLayoutStatusErrorBinding.inflate(LayoutInflater.from(context), statusView, false)
        }
        return viewBinding!!.root
    }

    override fun setRetryClick(listener: View.OnClickListener?) {
        viewBinding?.errorRetryView?.setOnClickListener(listener)
    }

    override fun showMessage(message: String) {
        viewBinding?.tvErrorHint?.text = message
    }

}

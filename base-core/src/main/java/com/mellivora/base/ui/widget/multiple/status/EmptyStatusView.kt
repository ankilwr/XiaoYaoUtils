package com.mellivora.base.ui.widget.multiple.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mellivora.base.databinding.BaseLayoutStatusEmptyBinding
import com.mellivora.base.ui.widget.multiple.MultipleStatusView
import com.mellivora.base.ui.widget.multiple.api.MultipleStatus

/**
 * 空布局试图
 */
class EmptyStatusView : MultipleStatus {

    private var viewBinding: BaseLayoutStatusEmptyBinding? = null

    override fun onCreateView(context: Context, statusView: MultipleStatusView): View {
        if (viewBinding == null) {
            viewBinding = BaseLayoutStatusEmptyBinding.inflate(LayoutInflater.from(context), statusView, false)
        }
        return viewBinding!!.root
    }

    override fun setRetryClick(listener: View.OnClickListener?) {
        viewBinding?.emptyView?.setOnClickListener(listener)
    }

    override fun showMessage(message: String) {
        viewBinding?.tvErrorHint?.text = message
    }

}

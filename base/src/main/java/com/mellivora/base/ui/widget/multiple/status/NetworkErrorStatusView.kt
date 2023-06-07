package com.mellivora.base.ui.widget.multiple.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mellivora.base.databinding.BaseLayoutStatusNetworkErrorBinding
import com.mellivora.base.ui.widget.multiple.MultipleStatusView
import com.mellivora.base.ui.widget.multiple.api.MultipleStatus

/**
 * 网络错误试图
 */
class NetworkErrorStatusView : MultipleStatus {

    private var viewBinding: BaseLayoutStatusNetworkErrorBinding? = null

    override fun onCreateView(context: Context, statusView: MultipleStatusView): View {
        if (viewBinding == null) {
            viewBinding = BaseLayoutStatusNetworkErrorBinding.inflate(LayoutInflater.from(context), statusView, false)
        }
        return viewBinding!!.root
    }

    override fun setRetryClick(listener: View.OnClickListener?) {
        viewBinding?.root?.setOnClickListener(listener)
    }

    override fun showMessage(message: String) {
        viewBinding?.tvNetworkErrorHint?.text = message
    }

}

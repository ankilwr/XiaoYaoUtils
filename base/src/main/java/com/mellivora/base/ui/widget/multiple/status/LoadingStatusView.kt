package com.mellivora.base.ui.widget.multiple.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mellivora.base.databinding.BaseLayoutStatusLoadingBinding
import com.mellivora.base.ui.widget.multiple.MultipleStatusView
import com.mellivora.base.ui.widget.multiple.api.MultipleStatus

/**
 * 加载中试图
 */
class LoadingStatusView : MultipleStatus {

    private var viewBinding: BaseLayoutStatusLoadingBinding? = null

    override fun onCreateView(context: Context, statusView: MultipleStatusView): View {
        if (viewBinding == null) {
            viewBinding = BaseLayoutStatusLoadingBinding.inflate(LayoutInflater.from(context), statusView, false)
        }
        return viewBinding!!.root
    }

    override fun showMessage(message: String) {
        viewBinding?.tvLoading?.text = message
    }

}

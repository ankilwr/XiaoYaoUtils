package com.mellivora.base.ui.widget.multiple.api

import android.content.Context
import android.view.View
import com.mellivora.base.ui.widget.multiple.MultipleStatusView


/**
 * View的状态切换
 */
interface MultipleStatus {

    /**
     * 获取当前视图
     */
    fun onCreateView(context: Context, statusView: MultipleStatusView): View

    /**
     * 显示提示信息
     * @param message: 要显示的内容
     */
    fun showMessage(message: String)

    /**
     * 设置重新加载的监听
     */
    fun setRetryClick(listener: View.OnClickListener?){

    }


}
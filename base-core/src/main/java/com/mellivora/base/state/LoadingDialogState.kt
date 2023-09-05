package com.mellivora.base.state

import kotlinx.coroutines.Job

/**
 * 加载中...弹窗状态事件
 */
sealed class LoadingDialogEvent{

    /**
     * 展示Loading弹窗
     */
    data class LoadingDialogShowEvent(
        var job: Job? = null,
        var cancelEnable: Boolean = true,
        var message: String? = null
    ): LoadingDialogEvent()

    /**
     * 关闭Loading弹窗
     */
    object LoadingDialogCloseEvent: LoadingDialogEvent()


}
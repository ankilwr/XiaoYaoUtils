package com.mellivora.base.state

import com.mellivora.base.exception.ErrorStatus

/**
 * 列表刷新状态
 */
data class PullState(
    var isPull: Boolean = false,
    var isRefresh: Boolean = true,
    var hasMore: Boolean = false,
    var loadingState: LoadingState = LoadingState.NONE,
    var message: String? = null,
    var code: Int = ErrorStatus.SERVER_ERROR
)
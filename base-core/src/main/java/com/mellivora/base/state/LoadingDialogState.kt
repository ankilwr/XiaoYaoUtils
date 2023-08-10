package com.mellivora.base.state

import kotlinx.coroutines.Job

/**
 * 加载框状态
 */
class LoadingDialogState(
    var isShow: Boolean = false,
    var job: Job? = null,
    var cancelEnable: Boolean = true,
    var message: String? = null
)

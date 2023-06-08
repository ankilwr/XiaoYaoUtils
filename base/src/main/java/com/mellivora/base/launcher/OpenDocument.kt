package com.mellivora.base.launcher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

/**
 * 文件管理选择器
 */
class OpenDocument : ActivityResultContract<Array<String>, Uri?>() {
    @CallSuper
    override fun createIntent(context: Context, input: Array<String>): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT)
            .putExtra(Intent.EXTRA_MIME_TYPES, input)
            .setType("*/*")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
    }
}
package com.mellivora.base.compose.ui.tools

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * 参数预览：字符串
 */
class StringParamsProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = listOf("测试测试").asSequence()
}
package com.mellivora.base.binding.ui.widget.multiple.api;


import android.content.Context;

import com.mellivora.base.binding.ui.widget.multiple.MultipleStatusView;


/**
 * 网络错误布局构建器
 */
public interface StatusViewCreator {
    MultipleStatus createStatusView(Context context, MultipleStatusView layout);
}
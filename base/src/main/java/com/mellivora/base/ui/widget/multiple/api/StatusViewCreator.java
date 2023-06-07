package com.mellivora.base.ui.widget.multiple.api;


import android.content.Context;

import com.mellivora.base.ui.widget.multiple.MultipleStatusView;
import com.mellivora.base.ui.widget.multiple.api.MultipleStatus;

/**
 * 网络错误布局构建器
 */
public interface StatusViewCreator {
    MultipleStatus createStatusView(Context context, MultipleStatusView layout);
}
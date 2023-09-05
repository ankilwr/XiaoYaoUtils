package com.shihang.kotlin

import android.app.Application
import com.mellivora.base.AppManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class MyApplication: Application() {

    /**
     * 全局初始化刷新控件的刷新样式
     * 整个APP尽量统一样式，有特殊需求的可以在指定页面修改样式
     */
    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(AppManager.getUiLifeListener())
    }

}
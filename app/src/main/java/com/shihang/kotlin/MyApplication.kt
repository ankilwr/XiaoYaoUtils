package com.shihang.kotlin

import android.app.Application
import com.shihang.kotlin.utils.FileUtils
import com.zhy.autolayout.config.AutoLayoutConifg

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FileUtils.init(this)
        AutoLayoutConifg.getInstance().useDeviceSize()
    }
}

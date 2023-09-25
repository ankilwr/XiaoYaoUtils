package com.mellivora.compose_app

import android.app.Application
import com.mellivora.base.AppManager

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(AppManager.getUiLifeListener())
    }

}
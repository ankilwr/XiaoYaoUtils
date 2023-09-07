package com.mellivora.base.compose.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope

abstract class BaseComposeActivity: AppCompatActivity() {

    private var isLazyLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { InitMainComposeView() }
    }

    @Composable
    abstract fun InitMainComposeView()

}
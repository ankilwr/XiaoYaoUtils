package com.mellivora.base.compose.attr

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


@Composable
fun rememberOnDefaultBackClick(): ()->Unit {
    val context = LocalContext.current
    return remember {
        {
            if (context is Activity) {
                context.finish()
            }
        }
    }
}
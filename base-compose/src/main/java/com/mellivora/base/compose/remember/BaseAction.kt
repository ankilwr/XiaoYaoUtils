package com.mellivora.base.compose.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.mellivora.base.expansion.getActivity


@Composable
fun rememberOnDefaultBackClick(): ()->Unit {
    val context = LocalContext.current
    return remember {
        { context.getActivity()?.finish() }
    }
}
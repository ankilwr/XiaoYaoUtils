package com.mellivora.base.ui.widget.attr

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.mellivora.base.ui.widget.attr.ComposeColor.Blue200
import com.mellivora.base.ui.widget.attr.ComposeColor.Blue300
import com.mellivora.base.ui.widget.attr.ComposeColor.Blue700
import com.mellivora.base.ui.widget.attr.ComposeColor.Blue800
import com.mellivora.base.ui.widget.attr.ComposeColor.Blue900


//============================  主题  =============================
/**
 * 日光主题颜色
 */
val LightThemeColors = lightColors(
    primary = Blue700,
    primaryVariant = Blue900,
    onPrimary = Color.White,
    secondary = Blue700,
    secondaryVariant = Blue200,
    onSecondary = Color.White,
    error = Blue800,
    onBackground = Color.Black,
)

/**
 * 暗黑主题颜色
 */
val DarkThemeColors = darkColors(
    primary = Blue300,
    primaryVariant = Blue700,
    onPrimary = Color.Black,
    secondary = Blue300,
    onSecondary = Color.Black,
    error = Blue200,
    onBackground = Color.White
)








package com.mellivora.base.ui.widget.attr

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors


//============================  主题  =============================
/**
 * 日光主题颜色
 */
val LightThemeColors = lightColors(
    primary = ComposeColor.RedThemeColors.themeColor,
    primaryVariant = ComposeColor.RedThemeColors.themeColor,
    onPrimary = ComposeColor.RedThemeColors.themeColor,
    onBackground = ComposeColor.RedThemeColors.backgroundColor,
    background = ComposeColor.RedThemeColors.backgroundColor
)

/**
 * 暗黑主题颜色
 */
val DarkThemeColors = darkColors(
    primary = ComposeColor.DarkColors.themeColor,
    primaryVariant = ComposeColor.DarkColors.themeColor,
    onPrimary = ComposeColor.DarkColors.themeColor,
    onBackground = ComposeColor.DarkColors.backgroundColor,
    background = ComposeColor.DarkColors.backgroundColor
)






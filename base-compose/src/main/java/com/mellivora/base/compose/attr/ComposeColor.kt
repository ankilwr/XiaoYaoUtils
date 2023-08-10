package com.mellivora.base.compose.attr

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

sealed class ComposeColor {

    open val statusBarColor = Color(0xffff3c2f)
    open val themeColor = Color(0xffff3c2f)
    open val backgroundColor = Color(0xfff5f5f5)
    open val textColor333 = Color(0xff333333)
    open val textColor666 = Color(0xff666666)
    open val textColor999 = Color(0xff999999)
    open val white = Color(0xffffffff)
    open val dividerColor = Color(0xffe2e2e2)

    /**
     * 亮色主题色系
     */
    object RedThemeColors: ComposeColor()

    /**
     * 暗系主题色系
     */
    object DarkColors: ComposeColor(){
        override val statusBarColor = Color.Gray
        override val themeColor = Color.Gray
        override val backgroundColor = Color.Black
        override val textColor333 = Color(0xff999999)
        override val textColor666 = Color(0xff999999)
        override val textColor999 = Color(0xff999999)
        override val white = Color.Gray
        override val dividerColor = Color.Gray
    }


}

/**
 * MaterialTheme颜色的扩展
 */
val Colors.otherColor get() = if(isLight) ComposeColor.RedThemeColors else ComposeColor.DarkColors






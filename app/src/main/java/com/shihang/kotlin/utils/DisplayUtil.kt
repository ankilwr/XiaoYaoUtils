package com.shihang.kotlin.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

object DisplayUtil {

    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun getWindowWidth(context: Activity): Int {
        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getWindowHeigth(context: Activity): Int {
        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

}

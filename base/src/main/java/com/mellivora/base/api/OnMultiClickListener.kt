package com.mellivora.base.api

import android.view.View
import java.util.*

/**
 * **Create Date:** 2020/7/28<br></br>
 * **Author:** xiaoyao<br></br>
 * **Description:多点击事件，防止用户时间段内多次点击导致事件多次执行</br>
 */
class OnMultiClickListener(private val click: (View)->Unit) : View.OnClickListener {

    constructor(clickListener: View.OnClickListener): this(clickListener::onClick)

    companion object{
        private val MIN_CLICK_DELAY_TIME = 300
        private var lastClickTime: Long = 0
    }

    override fun onClick(v: View) {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            click(v)
        }
    }

}

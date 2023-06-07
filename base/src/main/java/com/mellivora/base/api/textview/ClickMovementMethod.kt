package com.mellivora.base.api.textview

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.TextView


class ClickMovementMethod : LinkMovementMethod() {

    companion object {
        private var sInstance: ClickMovementMethod? = ClickMovementMethod()
        val instance: ClickMovementMethod
            get() {
                if (sInstance == null) sInstance = ClickMovementMethod()
                return sInstance!!
            }
    }

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        // 因为TextView没有点击事件，所以点击TextView的非富文本时，super.onTouchEvent()返回false；
        // 此时可以让TextView的父容器执行点击事件；
        val isConsume = super.onTouchEvent(widget, buffer, event)
        if (!isConsume && event.action == MotionEvent.ACTION_UP) {
            val parent = widget.parent
            if (parent is ViewGroup) {
                // 获取被点击控件的父容器，让父容器执行点击；
                parent.performClick()
            }
        }
        //带有选中状态的用该方式
        if(isConsume && event.action == MotionEvent.ACTION_UP) widget.isSelected = !widget.isSelected
        return isConsume
    }
}
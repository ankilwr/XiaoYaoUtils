@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.mellivora.base.expansion

import android.app.Activity
import android.graphics.Outline
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import com.mellivora.base.api.OnMultiClickListener
import com.mellivora.base.utils.dp
import kotlin.math.min


inline fun View.gone(): View{
    this.visibility = View.GONE
    return this
}

inline fun View.visible(): View{
    this.visibility = View.VISIBLE
    return this
}

inline fun View.invisible(): View{
    this.visibility = View.INVISIBLE
    return this
}

inline fun View.visibleOrGone(enable: Boolean): View{
    this.visibility = if(enable) View.VISIBLE else View.GONE
    return this
}

inline fun View.setMarginTop(top: Int): View{
    if(this.layoutParams == null) return this
    (this.layoutParams as ViewGroup.MarginLayoutParams).topMargin = top
    requestLayout()
    return this
}

inline fun View.visibleOrUnVisible(enable: Boolean): View{
    this.visibility = if(enable) View.VISIBLE else View.INVISIBLE
    return this
}



inline fun View.waitForLayout(crossinline finish: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            finish()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

inline fun View.waitForHeight(crossinline finish: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if(height > 0){
                finish()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

inline fun View.waitForWidth(crossinline finish: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if(width > 0){
                finish()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

fun View.setMultipleClick(click: (View) -> Unit) {
    setOnClickListener(OnMultiClickListener(click))
}

val View.layoutInflater: LayoutInflater
    get() {
        return LayoutInflater.from(context)
    }


val View.childFragmentManager: FragmentManager?
    get() {
        try {
            findFragment<Fragment>().also {
                return it.childFragmentManager
            }
        } catch (_: Throwable) {

        }
        if (context is FragmentActivity) {
            return (context as FragmentActivity).supportFragmentManager
        }
        return null
    }

val View.parentFragmentManager: FragmentManager?
    get() {
        try {
            findFragment<Fragment>().also {
                return it.parentFragmentManager
            }
        } catch (_: Throwable) {

        }
        context.getActivity()?.let {
            if(it is FragmentActivity){
                return it.supportFragmentManager
            }
        }
        return null
    }

val View.activity : Activity?
    get(){
        return this.context.getActivity()
    }

fun ViewGroup.inflate(layout: Int): View{
    return LayoutInflater.from(context).inflate(layout, this, false)
}

/**
 * @param radius: round isDp
 */
fun View.round(radius: Float){
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius.dp)
        }
    }
    this.clipToOutline = true
}

fun View.circle(){
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, min(view.width/2, view.height/2).toFloat())
        }
    }
    this.clipToOutline = true
}

inline fun View.setShadow(alpha: Int? = null, offsetX: Int = 0, offsetY: Int = 0) {
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRect(offsetX, offsetY, width, height)
            if (alpha != null) outline.alpha = alpha * 1.0f / 0xff
        }
    }
}

inline fun ViewGroup.initStatusBar(){
    val statusBarHeight = context.getStatusBarHeight()
    setPadding(paddingStart, statusBarHeight, paddingEnd, paddingBottom)
}



package com.mellivora.base.binding.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAImageView

/**
 * 修复SVGA在RecyclerView里被回收，再次复用加载时不显示的问题
 */
class SvgaFixImageView : SVGAImageView {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    private var isGestureTouch = false

    private val mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return performClick()
        }
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    })

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(drawable != null && drawable is SVGADrawable){
            startAnimation()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //修复SVGAImageView加载非Svg图片时，点击事件失效的bug
        if(hasOnClickListeners()){
            if(event?.action == MotionEvent.ACTION_DOWN){
                if(drawable == null || drawable !is SVGADrawable){
                    isGestureTouch = true
                }
            }
            val result = if(isGestureTouch && event != null){
                mGestureDetector.onTouchEvent(event)
            }else{
                super.onTouchEvent(event)
            }
            if(event?.action == MotionEvent.ACTION_UP){
                isGestureTouch = false
            }
            return result
        }
        return super.onTouchEvent(event)
    }

}
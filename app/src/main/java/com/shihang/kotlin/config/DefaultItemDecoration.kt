package com.shihang.kotlin.config

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

import com.shihang.kotlin.R
import com.yanzhenjie.recyclerview.swipe.SwipeAdapterWrapper
import com.zhy.autolayout.utils.AutoUtils


/**
 * 支持自定义线条颜色
 * 支持自定义线条高度
 * 支持自定义线条左右边距
 * 支持自定义线条边距颜色
 * 支持自定义最后一个item线条显示开关
 * @see setFooterLine()
 */
class DefaultItemDecoration constructor(context: Context,
                                        dividerColor: Int = R.color.line,
                                        private val dividerHeight: Int = AutoUtils.getPercentHeightSizeBigger(1),
                                        private val padding: Int = 0,
                                        paddingColor: Int = android.R.color.white) : RecyclerView.ItemDecoration() {

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bgPaint: Paint? = null //线条左右边距颜色
    private var footerLine = true
    private var footerLineFill = true

    /**
     * 设置最后一个item的线条
     * @param footerLine: 是否显示最后一个item的线条
     * @param footerLineFill: 当设置了线条边距的时候最后一个item的线条是否需要绘制边距
     */
    fun setFooterLine(footerLine: Boolean, footerLineFill: Boolean = true): DefaultItemDecoration {
        this.footerLineFill = footerLineFill
        this.footerLine = footerLine
        return this
    }


    init {
        //初始化线条颜色画笔
        mPaint.color = context.resources.getColor(dividerColor)
        mPaint.style = Paint.Style.FILL
        //如果设置了线条边距(padding>0表示线条有边距)，初始化线条边距画笔
        if (padding > 0)  bgPaint = Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
        bgPaint?.color = context.resources.getColor(paddingColor)
        bgPaint?.style = Paint.Style.FILL
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            //不是dataItem(添加的头部item，底部item)continue
            if(!isDataItem(parent, childView)) continue
            val isLastItem = isLastDataItem(parent, childView)
            //是最后一个dataItem,但是不需要下划线continue
            if(isLastItem && !footerLine) continue
            //下面开始绘制下划线
            val params = childView.layoutParams as RecyclerView.LayoutParams
            val top = childView.bottom + params.bottomMargin
            val bottom = top + dividerHeight
            if (bgPaint != null) { //当设置了线条边距的时候绘制边距
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), bgPaint)
            }
            if (footerLineFill && isLastItem) {
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            } else {
                c.drawRect((left + padding).toFloat(), top.toFloat(), (right - padding).toFloat(), bottom.toFloat(), mPaint!!)
            }
        }
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (!footerLine && isLastDataItem(parent, view)) {
            //footerLine = false, 不需要绘制最后一个item的下划线
            outRect.set(0, 0, 0, 0)
        } else {
            outRect.set(0, 0, 0, dividerHeight)
        }
    }

    private fun isDataItem(parent: RecyclerView, item: View): Boolean{
        val adapter = parent.adapter
        return when(adapter){
            is SwipeAdapterWrapper->{
                val childAdapterPosition = parent.getChildAdapterPosition(item)
                childAdapterPosition >= adapter.headerItemCount && childAdapterPosition < parent.adapter.itemCount - adapter.footerItemCount
            }
            else -> true
        }
    }

    private fun isLastDataItem(parent: RecyclerView, item: View): Boolean {
        val adapter = parent.adapter
        val childAdapterPosition = parent.getChildAdapterPosition(item)
        return when(adapter){
            is SwipeAdapterWrapper -> childAdapterPosition == adapter.itemCount - 1 - adapter.footerItemCount
            else -> childAdapterPosition == adapter.itemCount - 1
        }
    }
}
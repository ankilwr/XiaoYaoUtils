package com.mellivora.base.binding.ui.widget.divider

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.mellivora.base.utils.dp

open class LinearDecoration : RecyclerView.ItemDecoration() {

    internal val dividerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    internal val borderRect = Rect()

    private var dividerSize: Int = 0
    private val bgPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var dividerPaddingStart = 0
    private var dividerPaddingEnd = 0


    init {
        dividerPaint.color = Color.TRANSPARENT
        dividerPaint.style = Paint.Style.FILL
        bgPaint.color = Color.TRANSPARENT
        bgPaint.style = Paint.Style.FILL
    }


    open fun setColor(@ColorInt color: Int): LinearDecoration {
        dividerPaint.color = color
        return this
    }

    open fun setBorder(borderLeft: Int, borderTop: Int = borderLeft, borderRight: Int = borderLeft, borderBottom: Int = borderLeft): LinearDecoration {
        borderRect.set(borderLeft, borderTop, borderRight, borderBottom)
        return this
    }

    open fun setBorderDp(leftDp: Float, topDp: Float = leftDp, rightDp: Float = leftDp, bottomDp: Float = leftDp): LinearDecoration {
        borderRect.set(leftDp.dp.toInt(), topDp.dp.toInt(), rightDp.dp.toInt(), bottomDp.dp.toInt())
        return this
    }

    open fun setBorderTop(sizeDp: Float): LinearDecoration {
        borderRect.top = sizeDp.dp.toInt()
        return this
    }

    open fun setBorderBottom(sizeDp: Float): LinearDecoration {
        borderRect.bottom = sizeDp.dp.toInt()
        return this
    }



    fun setSizeDp(dividerSize: Float): LinearDecoration {
        this.dividerSize = dividerSize.dp.toInt()
        return this
    }

    fun setSize(dividerSize: Int): LinearDecoration {
        this.dividerSize = dividerSize
        return this
    }

    fun setDivider(@ColorInt paddingColor: Int, @ColorInt dividerColor: Int, dividerSize: Int, start: Int, end: Int = start): LinearDecoration {
        dividerPaddingStart = start
        dividerPaddingEnd = end
        setSize(dividerSize)
        setColor(dividerColor)
        bgPaint.color = paddingColor
        return this
    }

    private fun isLastItem(parent: RecyclerView, item: View): Boolean {
        val childAdapterPosition = parent.getChildAdapterPosition(item)
        return childAdapterPosition == parent.adapter!!.itemCount - 1
    }

    private fun isFirstItem(parent: RecyclerView, item: View): Boolean {
        val childAdapterPosition = parent.getChildAdapterPosition(item)
        return childAdapterPosition == 0
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager
        if (layoutManager == null || layoutManager !is LinearLayoutManager) {
            return
        }
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)

            val dividerLeft: Float
            val dividerRight: Float
            val dividerTop: Float
            val dividerBottom: Float
            val outRect = Rect()
            getOffsets(outRect, view, parent)

            if ((parent.layoutManager as LinearLayoutManager).orientation == LinearLayoutManager.VERTICAL) {
                dividerTop = view.top.toFloat() - outRect.top
                dividerBottom = view.top.toFloat()
                dividerLeft = (view.left + dividerPaddingStart).toFloat()
                dividerRight = (view.right - dividerPaddingEnd).toFloat()
                c.drawRect(view.left.toFloat(), dividerTop, view.right.toFloat(), dividerBottom, bgPaint)
            } else {
                dividerLeft = view.left.toFloat() - outRect.left
                dividerRight = view.left.toFloat()
                dividerTop = (view.top + dividerPaddingStart).toFloat()
                dividerBottom = (view.bottom - dividerPaddingEnd).toFloat()
                c.drawRect(dividerLeft, view.top.toFloat(), dividerRight, view.bottom.toFloat(), bgPaint)
            }
            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, dividerPaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        getOffsets(outRect, view, parent)
    }

    private fun getOffsets(outRect: Rect, view: View, parent: RecyclerView){
        val layoutManager = parent.layoutManager
        if (layoutManager == null || layoutManager !is LinearLayoutManager) {
            return
        }
        outRect.set(borderRect)
        if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = if(isFirstItem(parent, view)) borderRect.left else dividerSize
            outRect.right = if(isLastItem(parent, view)) borderRect.right else 0
        } else {
            outRect.top = if(isFirstItem(parent, view)) borderRect.top else dividerSize
            outRect.bottom = if(isLastItem(parent, view)) borderRect.bottom else 0
        }
    }
}

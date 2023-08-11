package com.mellivora.base.binding.ui.widget.divider

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mellivora.base.utils.dp
import kotlin.math.roundToInt

class GridDecoration : LinearDecoration() {

    private var horizontalSpace: Int = 0
    private var verticalSpace: Int = 0


    override fun setColor(@ColorInt color: Int): GridDecoration {
        super.setColor(color)
        return this
    }

    /**
     * 左右Border只对横向滚动并且Item宽度不填充为屏幕宽度的RecyclerView使用
     * (VERTICAL方向左右border设置无效)
     * VERTICAL方向请自行设置recycleview左右padding为左右border的边距解决)
     */
    override fun setBorder(borderLeft: Int, borderTop: Int, borderRight: Int, borderBottom: Int): GridDecoration {
        super.setBorder(borderLeft, borderTop, borderRight, borderBottom)
        return this
    }

    override fun setBorderDp(leftDp: Float, topDp: Float, rightDp: Float, bottomDp: Float): GridDecoration {
        super.setBorderDp(leftDp, topDp, rightDp, bottomDp)
        return this
    }

    override fun setBorderTop(sizeDp: Float): GridDecoration {
        super.setBorderTop(sizeDp)
        return this
    }

    override fun setBorderBottom(sizeDp: Float): GridDecoration {
        super.setBorderBottom(sizeDp)
        return this
    }

    fun setSizeDp(horizontalSpace: Float, verticalSpace: Float): GridDecoration {
        this.horizontalSpace = horizontalSpace.dp.toInt()
        this.verticalSpace = verticalSpace.dp.toInt()
        super.setSize(this.verticalSpace)
        return this
    }

    fun setSize(horizontalSpace: Int, verticalSpace: Int): GridDecoration {
        this.horizontalSpace = horizontalSpace
        this.verticalSpace = verticalSpace
        super.setSize(verticalSpace)
        return this
    }

    private fun getGridPosition(parent: RecyclerView, view: View): Int {
        return parent.getChildAdapterPosition(view)
    }

    private fun getGridItemCount(parent: RecyclerView): Int {
        return parent.adapter?.itemCount ?: 0
    }


    private fun isFirstRow(isVertical: Boolean, parent: RecyclerView, view: View): Boolean {
        val childAdapterPosition = getGridPosition(parent, view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        return if (isVertical) {
            childAdapterPosition < spanCount
        } else {
            childAdapterPosition % spanCount == 0
        }
    }

    private fun isLastRow(isVertical: Boolean, parent: RecyclerView, view: View): Boolean {
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val childAdapterPosition = getGridPosition(parent, view)
        val itemCount = getGridItemCount(parent)
        return if (isVertical) {
            val remainder = itemCount % spanCount
            childAdapterPosition >= itemCount - if (remainder == 0) spanCount else remainder
        } else {
            childAdapterPosition % spanCount == spanCount - 1
        }
    }

    private fun isFirstColumn(isVertical: Boolean, parent: RecyclerView, view: View): Boolean {
        val childAdapterPosition = getGridPosition(parent, view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        return if (isVertical) {
            childAdapterPosition % spanCount == 0
        } else {
            childAdapterPosition < spanCount
        }
    }

    private fun isLastColumn(isVertical: Boolean, parent: RecyclerView, view: View): Boolean {
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val childAdapterPosition = getGridPosition(parent, view)
        val itemCount = getGridItemCount(parent)
        return if (isVertical) {
            childAdapterPosition % spanCount == spanCount - 1
        } else {
            childAdapterPosition >= itemCount - itemCount % spanCount
        }
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager && layoutManager.spanCount == 1) {
            super.onDraw(c, parent, state)
            return
        }
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)

            val rectf = RectF()
            val rect = Rect()
            getItemOffsetF(rectf, view, parent, state)
            getItemOffsets(rect, view, parent, state)
            val left = view.left - rect.left.toFloat()
            val top = view.top - rect.top.toFloat()
            val right = view.right + rect.right.toFloat()
            val bottom = view.bottom + rect.bottom.toFloat()
            //绘制左边分割线
            c.drawRect(left, top, left + rectf.left, bottom, dividerPaint)
            //绘制上分割线
            c.drawRect(left, top, right, top + rectf.top, dividerPaint)
            //绘制右边分割线
            c.drawRect(right - rectf.right, top, right, bottom, dividerPaint)
            //绘制下分割线
            c.drawRect(left, bottom - rectf.bottom, right, bottom, dividerPaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        //super.getItemOffsets(outRect, view, parent, state)
        //精确的GridItem需要偏移的矩阵数据
        val rectF = RectF()
        getItemOffsetF(rectF, view, parent, state)
        outRect.left = rectF.left.roundToInt()
        outRect.right = rectF.right.roundToInt()
        outRect.top = rectF.top.toInt()
        outRect.bottom = rectF.bottom.toInt()
    }

    private fun getItemOffsetF(outRect: RectF, view: View, parent: RecyclerView, state: RecyclerView.State){
        val layoutManager = parent.layoutManager
        if (layoutManager !is GridLayoutManager)  {
            outRect.set(horizontalSpace.toFloat()/2, 0f, horizontalSpace.toFloat()/2, verticalSpace.toFloat())
            return
        }
        if(layoutManager.spanCount == 1){
            val rect = Rect()
            super.getItemOffsets(rect, view, parent, state)
            outRect.set(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat())
            return
        }
        val isVertical = layoutManager.orientation == LinearLayoutManager.VERTICAL
        val spanCount = layoutManager.spanCount
        if(isVertical){
            val adapterPosition = parent.getChildAdapterPosition(view)
            val eachWidth = (spanCount - 1) * horizontalSpace.toFloat() / spanCount
            outRect.left = adapterPosition % spanCount * (horizontalSpace - eachWidth)
            outRect.right = eachWidth - outRect.left
            outRect.top = 0f
            outRect.bottom = verticalSpace.toFloat()
        }else{
            outRect.set(0f, 0f, horizontalSpace.toFloat(), verticalSpace.toFloat())
        }
        //只接受2列并且左右border间距相等的边框分割线(否则左右边界线不做偏移)
        if (spanCount == 2 && borderRect.left == borderRect.right) {
            if (isFirstColumn(isVertical, parent, view)) outRect.left = outRect.left + borderRect.left
            if (isLastColumn(isVertical, parent, view)) outRect.right = outRect.right + borderRect.right
        }
        if (isFirstRow(isVertical, parent, view)) outRect.top = borderRect.top.toFloat()
        if (isLastRow(isVertical, parent, view)) outRect.bottom = borderRect.bottom.toFloat()
    }
}
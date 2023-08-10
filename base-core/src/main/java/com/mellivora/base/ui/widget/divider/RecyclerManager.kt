package com.mellivora.base.ui.widget.divider

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexLine
import com.google.android.flexbox.FlexboxLayoutManager


object RecyclerManager {

    fun getGridDecoration(horizontalSpace: Float = 0f, verticalSpace: Float = 0f): GridDecoration {
        return GridDecoration().setSizeDp(horizontalSpace, verticalSpace)
    }

    fun getLinearDecoration(space: Float = 0f): LinearDecoration {
        return LinearDecoration().setSizeDp(space)
    }

    fun getFlexboxLayoutManager(): Function1<View, RecyclerView.LayoutManager>{
        return object : Function1<View, RecyclerView.LayoutManager> {
            override fun invoke(view: View): RecyclerView.LayoutManager {
                return FlexboxLayoutManager(view.context)
            }
        }
    }

    fun getFlexboxLayoutManager(maxLines: Int): (View) -> RecyclerView.LayoutManager {
        return object : Function1<View, RecyclerView.LayoutManager> {
            override fun invoke(view: View): RecyclerView.LayoutManager {
                return LabelLayoutManager(view.context, maxLines)
            }
        }
    }
}

class LabelLayoutManager(context: Context, val maxLines: Int): FlexboxLayoutManager(context){

    /**
     * 这里限制了最大行数，多出部分被以 subList 方式截掉
     */
    override fun getFlexLinesInternal(): List<FlexLine>? {
        val flexLines = super.getFlexLinesInternal()
        val size = flexLines.size
        if (maxLines in 1 until size) {
            flexLines.subList(maxLines, size).clear()
        }
        return flexLines
    }
}
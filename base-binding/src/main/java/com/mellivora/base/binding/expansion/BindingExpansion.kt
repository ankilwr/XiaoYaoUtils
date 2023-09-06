package com.mellivora.base.binding.expansion

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.mellivora.base.R
import com.mellivora.base.binding.adapter.BaseMultiTypeAdapter
import com.mellivora.base.binding.ui.widget.multiple.MultipleStatusView
import com.mellivora.base.exception.ErrorStatus
import com.mellivora.base.expansion.appendColorText
import com.mellivora.base.expansion.appendSizeText
import com.mellivora.base.expansion.editable
import com.mellivora.base.expansion.getResString
import com.mellivora.base.expansion.getStateDrawable
import com.mellivora.base.expansion.round
import com.mellivora.base.expansion.setMultipleClick
import com.mellivora.base.expansion.strikeLine
import com.mellivora.base.expansion.underLine
import com.mellivora.base.state.LoadingState
import com.mellivora.base.state.PullState
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlin.math.max

object BindingExpansion {

    //==============================================================================================
    @JvmStatic
    @BindingAdapter("bindSpanCount")
    fun setRvAdapter(recyclerView: RecyclerView, count: Int?) {
        val layoutManager = recyclerView.layoutManager
        if(layoutManager != null && layoutManager is GridLayoutManager){
            layoutManager.spanCount = max(1, count ?: 1)
        }
    }

    @JvmStatic
    @BindingAdapter("bindItemDecoration", requireAll = false)
    fun setRvDecoration(recyclerView: RecyclerView, decoration: RecyclerView.ItemDecoration?){
        if(recyclerView.itemDecorationCount == 0 && decoration != null){
            recyclerView.addItemDecoration(decoration)
        }
    }

    @JvmStatic
    @BindingAdapter("bindItemAnimator", requireAll = false)
    fun setRvItemAnimator(recyclerView: RecyclerView, animator: RecyclerView.ItemAnimator?){
        recyclerView.itemAnimator = animator
    }

    @JvmStatic
    @BindingAdapter("bindLayoutManager")
    fun setRvAdapter(recyclerView: RecyclerView, layoutManager: Function1<View, RecyclerView.LayoutManager>?){
        //layoutManager: ((View) -> RecyclerView.LayoutManager)? = null
        //layoutManager: Function1<View, RecyclerView.LayoutManager>?
        layoutManager?.let {
            recyclerView.layoutManager = it.invoke(recyclerView)
        }
    }

    @JvmStatic
    @BindingAdapter("bindAdapter", "bindItemList", "bindNotifyAll", requireAll = false)
    fun setRvAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?, list: MutableList<*>?, notifyAll: Boolean?) {
        if(recyclerView.adapter == null){
            recyclerView.adapter = adapter
        }
        adapter?.let {
            val newItemCounts = list?.size ?: 0
            val oldItemsCount = it.itemCount

            list as MutableList<Any>?
            if(notifyAll == true && it is MultiTypeAdapter){
                it.items = list ?: emptyList()
                it.notifyItemRangeChanged(0, it.itemCount)
                return
            }
            when(it){
                is BaseMultiTypeAdapter -> it.resetNotify(list)
                is MultiTypeAdapter -> it.items = list ?: emptyList()
                else -> {
                    val builder = StringBuilder("请检查adapter类型, bindAdapter类型只能为以下几种类型:")
                    builder.append("1.\n${MultiTypeAdapter::class.java.name}")
                    builder.append("3.\n${BaseMultiTypeAdapter::class.java.name}")
                    throw java.lang.ClassCastException(builder.toString())
                }
            }

            //由于分割线的问题，添加数据后，最后一个Item也要刷新一下，重新绘制一下分割线
            if(oldItemsCount in 1 until newItemCounts){
                it.notifyItemChanged(oldItemsCount - 1)
            }
        }
    }


    @JvmStatic
    @BindingAdapter("bindPullState")
    fun pullState(refreshView: SmartRefreshLayout, state: PullState?){
        if(state == null) return
        if(state.isPull){
            if(state.isRefresh){
                refreshView.finishRefresh(state.loadingState == LoadingState.SUCCESS)
            }else{
                refreshView.finishLoadMore(state.loadingState == LoadingState.SUCCESS)
            }
        }
        if(state.loadingState == LoadingState.SUCCESS){
            refreshView.setNoMoreData(!state.hasMore)
        }
    }

    @JvmStatic
    @BindingAdapter("bindOnRefresh")
    fun pullState(refreshView: SmartRefreshLayout, listener: OnRefreshListener?){
        refreshView.setOnRefreshListener(listener)
    }
    @JvmStatic
    @BindingAdapter("bindOnLoadMore")
    fun pullState(refreshView: SmartRefreshLayout, listener: OnLoadMoreListener?){
        refreshView.setOnLoadMoreListener(listener)
    }

    @JvmStatic
    @BindingAdapter("bindRefreshEnable", "bindLoadMoreEnable", requireAll = false)
    fun pullState(refreshView: SmartRefreshLayout, refreshEnable: Boolean?, loadMoreEnable: Boolean?){
        if(refreshEnable != null){
            refreshView.setEnableRefresh(refreshEnable)
        }
        if(loadMoreEnable != null){
            refreshView.setEnableLoadMore(loadMoreEnable)
        }
    }


    @JvmStatic
    @BindingAdapter("bindViewState", "bindNotEmpty", "bindEmptyText", requireAll = false)
    fun loadingState(view: MultipleStatusView, state: PullState?, bindNotEmpty: Boolean?, bindEmptyText: String? = ""){
        if(state == null) return
        //这里不判断isPull，有可能用户自行删除某条数据，导致列表为空(并非刷新or加载)
        if(state.loadingState == LoadingState.SUCCESS){
            //为什么要判断==null呢，因为==null的时候是没有使用bindNotEmpty这个属性,如果使用了，必定会有值
            if(bindNotEmpty == null || bindNotEmpty == true){
                view.showContent()
            }else{
                view.showEmpty(bindEmptyText?:"")
            }
        }
        //下拉刷新的方式加载的数据，该控件不做处理
        if(state.isPull){
            return
        }
        when (state.loadingState) {
            LoadingState.LOADING -> {
                val message = state.message ?: getResString(R.string.base_status_loading)
                view.showLoading(message)
            }
            LoadingState.ERROR -> {
                if(state.code == ErrorStatus.NETWORK_ERROR){
                    view.showNoNetwork(state.message)
                }else{
                    view.showError(state.message)
                }
            }
            else -> {

            }
        }
    }

    @JvmStatic
    @BindingAdapter("bindStateReloadClick")
    fun setOnReloadClick(view: MultipleStatusView, listener: OnClickListener?){
        view.setOnRetryClickListener(listener)
    }


    @JvmStatic
    @BindingAdapter("bindSelected")
    fun selected(view: View, selected: Boolean) {
        view.isSelected = selected
    }

    @JvmStatic
    @BindingAdapter("bindVisible")
    fun visible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("bindVisibility")
    fun visibility(view: View, invisible: Boolean) {
        view.visibility = if (!invisible) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("bindRippleDrawable", "bindRippleColor", requireAll = false)
    fun bindRipple(view: View, drawable: Drawable, color: Int?) {
        val rippleColor = color ?: Color.parseColor("#33000000")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.foreground = RippleDrawable(ColorStateList.valueOf(rippleColor), null, drawable)
        }
    }

    @JvmStatic
    @BindingAdapter("bindRoundSize")
    fun round(view: View, radiusSize: Float) {
        view.round(radiusSize)
    }

    @JvmStatic
    @BindingAdapter("bindText", "bindAppendText", "bindAppendColor", requireAll = true)
    fun textAppendColor(tv: TextView, normalText: CharSequence?, appendText: String?, appendColor: Int){
        tv.text = normalText?:""
        if(appendText != null){
            tv.appendColorText(appendText, appendColor)
        }
    }

    @JvmStatic
    @BindingAdapter("bindText", "bindAppendText", "bindAppendSize", requireAll = true)
    fun textAppendSize(tv: TextView, normalText: CharSequence?, appendText: CharSequence?, appendSize: Float){
        tv.text = normalText?:""
        if(appendText != null){
            tv.appendSizeText(appendText, appendSize.toInt())
        }
    }

    @JvmStatic
    @BindingAdapter("bindTextColor", "bindNormalColor", requireAll = true)
    fun setTextColor(tv: TextView, color: String?, normalColor: Int){
        try {
            val colorInt = Color.parseColor(color)
            tv.setTextColor(colorInt)
        }catch (e: Throwable){
            e.printStackTrace()
            tv.setTextColor(normalColor)
        }
    }



    /**
     * @param bindUnderLine: true, 下划线
     * @param bindStrikeLine: true, 中划线
     */
    @JvmStatic
    @BindingAdapter("bindStrikeLine", "bindUnderLine", requireAll = false)
    fun setTextLine(tv: TextView, bindStrikeLine: Boolean, bindUnderLine: Boolean){
        if(bindStrikeLine){
            tv.strikeLine()
        }
        if(bindUnderLine){
            tv.underLine()
        }
    }

    @JvmStatic
    @BindingAdapter("bindOnClick")
    fun setOnClick(view: View, clickListener: View.OnClickListener?) {
        if(clickListener == null){
            view.setOnClickListener(null)
        }else{
            view.setMultipleClick {
                clickListener.onClick(it)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("bindEditable")
    fun setEditable(view: EditText, enable: Boolean){
        view.editable(enable)
    }

    @JvmStatic
    @BindingAdapter("bindCheckNormalSrc", "bindCheckedSrc", "bindSrcGravity", requireAll = true)
    fun setCheckSrc(checkBox: CompoundButton, normalSrc: Drawable?, selected: Drawable?, gravity: String?){
        val drawable = getStateDrawable(android.R.attr.state_checked, normalSrc, selected)
        when(gravity){
            "left", "start" -> checkBox.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            "top" -> checkBox.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            "right", "end" -> checkBox.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            "bottom" -> checkBox.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
            "center" -> checkBox.buttonDrawable = drawable
        }
    }


}
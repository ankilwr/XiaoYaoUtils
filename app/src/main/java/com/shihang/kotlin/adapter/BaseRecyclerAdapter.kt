package com.shihang.kotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseRecyclerAdapter<T>(mContext: Context, private var beans: MutableList<T>?, private var noDataView: View? = null)
    : RecyclerView.Adapter<RecyclerHolder>() {


    val datas: MutableList<T>? get() = beans   //用于给外部调用的getDatas方法
    private var mContext: Context? = mContext



    /** 刷新布局
     * @see notifyDataSetChanged()
     */
    fun refreshItems() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (beans == null) 0 else beans!!.size
    }



    fun add(bean: T) {
        if(bean == null) return
        if(beans == null) beans = ArrayList()
        beans!!.add(bean)
        refreshItems()
    }

    fun remove(position: Int) {
        if (beans != null && beans!!.size > position && position >= 0) {
            beans!!.removeAt(position)
            refreshItems()
        }
    }


    fun addNotify(addBeans: MutableList<T>?) {
        if (beans == null) beans = addBeans else beans!!.addAll(addBeans!!)
        refreshItems()
    }

    fun resetNotify(resetBeans: MutableList<T>?) {
        beans = resetBeans
        refreshItems()
    }


    fun cleanNotify() {
        beans?.clear()
        refreshItems()
    }


}

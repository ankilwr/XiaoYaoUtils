package com.shihang.kotlin.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView

class RecyclerHolder(private val convertView: View) : RecyclerView.ViewHolder(convertView) {

    private val mViews: SparseArray<View> = SparseArray()

    fun setText(id: Int, text: CharSequence?): RecyclerHolder {
        (getView<TextView>(id))?.text = text ?: ""
        return this
    }

    fun setImageRes(id: Int, icon: Int): RecyclerHolder {
        (getView<ImageView>(id))?.setImageResource(icon)
        return this
    }

    fun setCheck(id: Int, check: Boolean): RecyclerHolder {
        (getView<View>(id) as Checkable).isChecked = check
        return this
    }

    fun setVisibility(id: Int, visible: Int): RecyclerHolder {
        getView<View>(id)?.visibility = visible
        return this
    }

    fun setOnClickListener(id: Int, listener: View.OnClickListener): RecyclerHolder {
        getView<View>(id)?.setOnClickListener(listener)
        return this
    }

    fun setOnLongClickListener(id: Int, listener: View.OnLongClickListener?): RecyclerHolder {
        getView<View>(id)?.setOnLongClickListener(listener)
        return this
    }

    fun <T : View> getView(viewId: Int): T? {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

}

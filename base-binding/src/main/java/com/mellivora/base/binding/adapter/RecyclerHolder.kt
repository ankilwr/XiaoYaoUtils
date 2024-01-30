package com.mellivora.base.binding.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * <b>Create Date:</b> 2020/7/10<br>
 * <b>Author:</b> xiaoyao<br>
 * <b>Description:</b><br>
 */

open class RecyclerHolder : RecyclerView.ViewHolder {

    constructor(convertView: View): super(convertView)

    constructor(viewBinding: ViewBinding): this(viewBinding.root){
        this.viewBinding = viewBinding
    }

    private val mViews: SparseArray<View> = SparseArray()
    private val mViewsBinding: SparseArray<ViewBinding> = SparseArray()
    private var viewBinding: ViewBinding? = null

    fun <T: ViewBinding> getBinding(): T{
        return viewBinding as T
    }

    fun getContext(): Context {
        return itemView.context
    }

    fun setText(id: Int, text: CharSequence?): RecyclerHolder {
        (getView<TextView>(id)).text = text ?: ""
        return this
    }
    fun setBackground(id: Int, res: Int): RecyclerHolder {
        (getView<TextView>(id)).setBackgroundResource(res)
        return this
    }

    fun setSelect(id: Int, isSelect: Boolean): RecyclerHolder {
        (getView<View>(id)).isSelected = isSelect
        return this
    }

    fun setText(id: Int, @StringRes text: Int): RecyclerHolder {
        (getView<TextView>(id)).setText(text)
        return this
    }

    fun setImageRes(id: Int, icon: Int): RecyclerHolder {
        if(icon == 0){
            setImageDrawable(id, null)
        }else{
            (getView<ImageView>(id)).setImageResource(icon)
        }
        return this
    }

    fun setImageDrawable(id: Int, icon: Drawable?): RecyclerHolder {
        (getView<ImageView>(id)).setImageDrawable(icon)
        return this
    }


    fun setCheck(id: Int, check: Boolean): RecyclerHolder {
        (getView<View>(id) as Checkable).isChecked = check
        return this
    }

    fun setVisibility(id: Int, visible: Int): RecyclerHolder {
        getView<View>(id).visibility = visible
        return this
    }

    fun setOnClickListener(id: Int, listener: View.OnClickListener?): RecyclerHolder {
        getView<View>(id).setOnClickListener(listener)
        return this
    }

    fun setOnLongClickListener(id: Int, listener: (view: View) -> Boolean): RecyclerHolder {
        getView<View>(id).setOnLongClickListener{ listener(it) }
        return this
    }

    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    fun <T: ViewBinding> saveChildBinding(viewId: Int, binding: T){
        mViewsBinding.put(viewId, binding)
    }

    fun <T: ViewBinding> getChildBinding(viewId: Int): T?{
        return mViewsBinding.get(viewId) as? T
    }

    fun getString(stringId: Int): String {
        return getContext().getString(stringId)
    }

    fun getString(stringId: Int, vararg formatString: Any): String {
        return getContext().getString(stringId, *formatString)
    }
}

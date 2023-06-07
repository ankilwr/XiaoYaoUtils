package com.mellivora.base.adapter

import androidx.recyclerview.widget.DiffUtil
import com.drakeet.multitype.MultiTypeAdapter
import java.util.Collections.addAll
import kotlin.math.max

open class BaseMultiTypeAdapter : MultiTypeAdapter() {

    var onChildItemClick: (childBean: Any)->Unit = {}


    //============================   查   ==================================
    /**
     * 获取Adapter里面的数据
     */
    inline fun <reified T: Any> getData(position: Int): T{
        return items[position] as T
    }

    /**
     * 获取数据所在列表位置
     */
    fun indexOf(bean: Any): Int{
        return items.indexOf(bean)
    }


    //============================    增   ==================================

    /**
     * 添加数据并刷新列表
     */
    fun addNotify(any: Any, animation: Boolean = false){
        addNotify(any, items.size, animation)
    }

    /**
     * 添加数据到指定位置并刷新列表
     */
    open fun addNotify(addBean: Any?, index: Int = items.size, animation: Boolean = false) {
        if(addBean == null) return
        val list = mutableListOf<Any>()
        list.addAll(items)
        list.add(index, addBean)
        resetNotify(list, animation)
    }

    /**
     * 添加数据列表到指定位置
     */
    open fun addList(list: MutableList<out Any>?, index: Int = items.size, animation: Boolean = false) {
        if (!list.isNullOrEmpty()) {
            val newList = mutableListOf<Any>()
            newList.addAll(items)
            newList.addAll(index, list)
            resetNotify(newList, animation)
        }
    }



    //============================    删   ==================================
    /**
     * 移除列表数据
     * @param position: 移除
     */
    open fun remove(position: Int, animation: Boolean = false) {
        val newList = mutableListOf<Any>()
        newList.addAll(items)
        newList.remove(position)
        resetNotify(newList, animation)
    }

    /**
     * 移除列表数据
     * @param data: 移除数据
     */
    fun remove(data: Any, animation: Boolean = false){
        val index = items.indexOf(data)
        if (index != -1) {
            remove(index, animation)
        }
    }

    /**
     * 清空数据列表
     */
    fun clear(){
        resetNotify(null, false)
    }




    //============================    改   ==================================
    /**
     * 数据存在:替换数据, 数据不存在: 添加至列表
     */
    open fun addOrUpdate(addBean: Any, animation: Boolean = false) {
        val index = items.indexOf(addBean)
        if (index != -1) {
            (items as MutableList)[index] = addBean
            notifyItemChanged(index)
        } else {
            addNotify(addBean, animation)
        }
    }

    /**
     * 数据存在:替换数据
     */
    open fun updateItem(addBean: Any) {
        val index = items.indexOf(addBean)
        if (index != -1) {
            notifyItemChanged(index)
        }
    }

    /**
     * 重置数据
     */
    fun resetNotify(list: List<*>?, animation: Boolean = true) {
        val newList = list as? List<Any> ?: arrayListOf()
        val resetList = if(newList == items){
            arrayListOf<Any>().apply { addAll(newList) }
        }else{
            newList
        }
        if(animation){
            val diffResult = DiffUtil.calculateDiff(DiffCallBack(items, resetList), true)
            diffResult.dispatchUpdatesTo(this)
        }else{
            items = resetList
            notifyItemRangeChanged(0, max(0, itemCount - 1))
        }
    }




    private inner class DiffCallBack(val oldList: List<*>, val newList: List<*>): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return 0
        }
    }
}
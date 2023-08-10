package com.mellivora.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * TabLayout + ViewPager关联管理Adapter(只需关注ViewPager即可，可动态添加fragment)
 * 泛型类型为 TabLayout 需要绑定数据的类型
 */
abstract class BaseFragmentPagerAdapter<T>: FragmentStateAdapter {

    constructor(activity: FragmentActivity): super(activity)
    constructor(fragment: Fragment): super(fragment)

    private val tabListData = mutableListOf<T>()

    abstract fun onCreateFragment(data: T, position: Int): Fragment

    open fun getFragmentTag(data: T, position: Int): Long{
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return tabListData.size
    }

    override fun getItemId(position: Int): Long {
        return getFragmentTag(getTab(position), position)
    }

    override fun containsItem(itemId: Long): Boolean {
        tabListData.forEachIndexed { index, _ ->
            if(getItemId(index) == itemId){
                return true
            }
        }
        return false
    }

    fun getTab(position: Int): T{
        return tabListData[position]
    }

    /**
     * 在Activity里面：使用supportFragmentManager
     * 在Fragment里面：使用childFragmentManager
     */
    fun getFragment(fragmentManager: FragmentManager, position: Int): Fragment?{
        return fragmentManager.findFragmentByTag("f$position")
    }

    override fun createFragment(position: Int): Fragment {
        val tab = getTab(position)
        return onCreateFragment(tab, position)
    }

    fun resetData(tabs: MutableList<T>?){
        tabListData.clear()
        tabs?.let{
            tabListData.addAll(it)
        }
        notifyItemRangeChanged(0, itemCount)
    }
}
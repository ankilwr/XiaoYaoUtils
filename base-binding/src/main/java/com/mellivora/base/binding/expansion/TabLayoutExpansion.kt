package com.mellivora.base.binding.expansion

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.mellivora.base.binding.adapter.BaseFragmentPagerAdapter
import com.mellivora.base.expansion.activity
import com.mellivora.base.expansion.tryBlock

/**
 * 关联TabLayout跟ViewPager的联动
 * @param viewPager: ViewPager视图
 * @param fms: 关联的Fragments
 * @param smoothScroll: 点击tab时切换ViewPager需不需要滚动动画
 * @param tabChangeListener: 当Tab为自定义的视图时可做对应状态的改变(tab: 需要操作的Tab, boolean:当前tab的选中状态)
 */
fun TabLayout.attachToPager(viewPager: ViewPager, fms: List<Fragment>, smoothScroll: Boolean = false, tabChangeListener:(Tab, Boolean)->Unit) {

}

/**
 * 关联TabLayout跟ViewPager的联动
 * @param viewPager: ViewPager视图
 * @param smoothScroll: 点击tab时切换ViewPager需不需要滚动动画
 * @param binder: 当Tab为自定义的视图时可做对应状态的改变
 */
fun TabLayout.attachToPager(
    viewPager: ViewPager2,
    smoothScroll: Boolean = false,
    binder: TabPagerBinder
) {
    addOnTabSelectedListener(object: OnTabSelectedListener{
        override fun onTabSelected(tab: Tab) {
            binder.onTabChange(tab, true)
        }
        override fun onTabUnselected(tab: Tab) {
            binder.onTabChange(tab, false)
        }
        override fun onTabReselected(tab: Tab?) {
        }
    })
    TabLayoutMediator(this, viewPager, true, smoothScroll) { tab, position ->
        binder.onTabCreate(tab, position)
    }.attach()
}

/**
 * 根据Tab的数据类型去创建对应的PagerFragment
 */
fun <T> TabLayout.attachToFragmentPager(
    viewPager: ViewPager2,
    smoothScroll: Boolean = true,
    cacheSize: Int = Short.MAX_VALUE.toInt(),
    getFragmentTag:((tab: T, position: Int) -> Long)? = null,
    binder: TabFragmentPagerBinder<T>
): BaseFragmentPagerAdapter<T> {
    //预加载的Fragment数量
    //viewPager.offscreenPageLimit = fms.size
    //需要缓存的Fragment视图数量，默认全部缓存
    viewPager.getRecyclerView()?.apply {
        setItemViewCacheSize(cacheSize)
    }
    val pagerAdapter = tryBlock {
        val fragment = viewPager.findFragment<Fragment>()
        object: BaseFragmentPagerAdapter<T>(fragment){
            override fun onCreateFragment(data: T, position: Int): Fragment {
                return binder.onCreateFragment(data, position)
            }
            override fun getFragmentTag(data: T, position: Int): Long {
                return getFragmentTag?.invoke(data, position) ?: super.getFragmentTag(data, position)
            }
        }
    } ?: run {
        val activity = viewPager.activity as FragmentActivity
        object: BaseFragmentPagerAdapter<T>(activity){
            override fun onCreateFragment(data: T, position: Int): Fragment {
                return binder.onCreateFragment(data, position)
            }
            override fun getFragmentTag(data: T, position: Int): Long {
                return getFragmentTag?.invoke(data, position) ?: super.getFragmentTag(data, position)
            }
        }
    }
    viewPager.adapter = pagerAdapter
    viewPager.isUserInputEnabled = smoothScroll
    attachToPager(viewPager, smoothScroll, binder)
    return pagerAdapter
}

/**
 * Tab + ViewPager(View)
 */
interface TabPagerBinder{
    /**
     * tab创建, 绑定tab的内容
     */
    fun onTabCreate(tab: Tab, position: Int)

    /**
     * tab切换, 自定义试图有特殊操作的话可用,默认视图可忽略
     */
    fun onTabChange(tab: Tab, checked: Boolean){}
}

/**
 * Tab + ViewPager(Fragment)
 */
interface TabFragmentPagerBinder<T>: TabPagerBinder {
    /**
     * 根据Tab的数据类型，创建对应的Fragment
     */
    fun onCreateFragment(tabData: T, position: Int): Fragment
}


fun ViewPager2.getRecyclerView(): RecyclerView?{
    for(i in 0 until childCount){
        val child = getChildAt(i)
        if(child is RecyclerView){
            return child
        }
    }
    return null
}




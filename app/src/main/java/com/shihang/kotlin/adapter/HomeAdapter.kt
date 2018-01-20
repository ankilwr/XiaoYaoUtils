package com.shihang.kotlin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class HomeAdapter(private val fmManager: FragmentManager, private val fms: List<Fragment>) : PagerAdapter() {

    override fun getCount(): Int {
        return fms.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //container.removeView(fragments.get(position).getView());
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any? {
        val fragment = fms[position]
        if (!fragment.isAdded) {
            val ft = fmManager.beginTransaction()
            ft.add(fragment, fragment.javaClass.simpleName)
            //ft.commit();
            ft.commitAllowingStateLoss()
            fmManager.executePendingTransactions()
        }
        if (fragment.view!!.parent == null) {
            container.addView(fragment.view)
        }
        return fragment.view
    }

}

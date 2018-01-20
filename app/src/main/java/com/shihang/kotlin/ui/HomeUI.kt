package com.shihang.kotlin.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.shihang.kotlin.R
import com.shihang.kotlin.adapter.HomeAdapter
import com.shihang.kotlin.fragment.MineFm
import com.shihang.kotlin.fragment.OnLineFm
import com.shihang.kotlin.view.HomeTabButton
import com.shihang.kotlin.view.HomeTabGroup.OnCheckedChangeListener
import kotlinx.android.synthetic.main.ui_home.*
import java.util.*


class HomeUI : BaseUI() {

    private val fms = ArrayList<Fragment>()

    private val tabListener = object : OnCheckedChangeListener {
        override fun onSelect(button: HomeTabButton, index: Int) {
            viewpager.setCurrentItem(index, false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val window = window
                if (index == 0) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = ContextCompat.getColor(this@HomeUI, R.color.actionBarColor)
                } else {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    window.statusBarColor = ContextCompat.getColor(this@HomeUI, R.color.themeColor)
                }
            }
            (fms[index] as OnCheckedChangeListener).onSelect(button, index)
        }

        override fun onUnSelect(button: HomeTabButton, index: Int) {
            (fms[index] as OnCheckedChangeListener).onUnSelect(button, index)
        }

        override fun onReSelect(button: HomeTabButton, index: Int) {
            (fms[index] as OnCheckedChangeListener).onReSelect(button, index)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNightStatus()
        setContentView(R.layout.ui_home)
        tabGroup.setOnChangeListener(tabListener, true)
        fms.add(OnLineFm())
        fms.add(MineFm())
        val adapter = HomeAdapter(supportFragmentManager, fms)
        viewpager.adapter = adapter
    }


}

package com.shihang.kotlin.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.shihang.kotlin.R
import com.shihang.kotlin.config.Keys
import com.shihang.kotlin.ui.LoginUI
import com.shihang.kotlin.utils.PreferenceUtils

/**
 * Created by Administrator on 2017/5/31.
 */
class WelcomeAdapter(private val context: Context) : PagerAdapter() {

    private val drawables: IntArray = intArrayOf(R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher)

    override fun getCount(): Int {
        return drawables.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //container.removeView(fragments.get(position).getView());
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.setBackgroundResource(drawables[position])
        if (position == drawables.size - 1) {
            imageView.setOnClickListener {
                PreferenceUtils.setPrefBoolean(context, Keys.IS_FIRST, false)
                val intent = Intent(context, LoginUI::class.java)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
        container.addView(imageView)
        return imageView
    }

}

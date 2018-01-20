package com.shihang.kotlin.fragment

import android.support.annotation.StringRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.shihang.kotlin.R


abstract class TitleFm : BaseFm() {

    var mTitleText: TextView? = null
    var back: ImageView? = null
    var rightImage: ImageView? = null
    var rightText: TextView? = null
    var actionBarLine: View? = null

    open fun leftClick(v: View) {}
    open fun rightClick(v: View) {}


    override fun initViews() {
        mTitleText = getView(R.id.titleText)
        back = getView(R.id.back)
        rightImage = getView(R.id.rightImage)
        rightText = getView(R.id.rightText)
        actionBarLine = getView(R.id.actionBarLine)
        fun titleBtnsClick(v: View){
            when (v.id) {
                R.id.back -> leftClick(v)
                R.id.rightImage -> rightClick(v)
                R.id.rightText -> rightClick(v)
            }
        }
        back?.setOnClickListener{ titleBtnsClick(it) }
        rightImage?.setOnClickListener{ titleBtnsClick(it) }
        rightText?.setOnClickListener{ titleBtnsClick(it) }
    }


    fun titleGoneAll() {
        showBack(false, 0)
        showRightImage(false, 0)
        showRightText(false, 0)
        showTitle(false, 0)
        showActionBarLine(false)
    }

    fun showBack(visible: Boolean, imageResourceId: Int) {
        back?.visibility = if(visible) View.VISIBLE else View.INVISIBLE
        back?.isClickable = visible
        if (imageResourceId != 0) back?.setImageResource(imageResourceId)
    }
    fun showRightImage(visible: Boolean, imageResourceId: Int) {
        rightImage?.visibility = if(visible) View.VISIBLE else View.INVISIBLE
        rightImage?.isClickable = visible
        if (imageResourceId != 0) rightImage?.setImageResource(imageResourceId)
    }
    fun showActionBarLine(show: Boolean) {
        actionBarLine?.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showTitle(visible: Boolean, @StringRes text: Int) {
        showTitle(visible, getStr(text))
    }
    fun showTitle(visible: Boolean, text: String) {
        mTitleText?.visibility = if (visible) View.VISIBLE else View.GONE
        mTitleText?.text = text
    }

    fun showRightText(visible: Boolean, @StringRes text: Int) {
        showRightText(visible, getStr(text))
    }
    fun showRightText(visible: Boolean, text: String) {
        rightText?.visibility = if (visible) View.VISIBLE else View.GONE
        rightText?.isClickable = visible
        rightText?.text = text
    }

}

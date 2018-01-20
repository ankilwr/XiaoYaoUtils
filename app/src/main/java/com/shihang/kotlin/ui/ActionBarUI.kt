package com.shihang.kotlin.ui

import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.ActionBar.LayoutParams
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import com.shihang.kotlin.R


open class ActionBarUI : BaseUI() {

    private var actionbarView: View? = null
    var mTitleText: TextView? = null
    var back: ImageView? = null
    var rightImage: ImageView? = null
    var rightText: TextView? = null
    var actionBarLine: View? = null

    private val clickListener = OnClickListener { v ->
        when (v.id) {
            R.id.back -> leftClick(v)
            R.id.rightImage -> rightClick(v)
            R.id.rightText -> rightClick(v)
        }
    }

    fun leftClick(v: View) {
        finish()
    }

    fun rightClick(v: View) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBarLayout(R.layout.title_theme)
    }


    fun setActionBarLayout(layoutId: Int) {
        val actionBar = supportActionBar
        if (null != actionBar) {
            if (Build.VERSION.SDK_INT >= 21) actionBar.elevation = 0f
            actionBar.setDisplayShowHomeEnabled(false)
            actionBar.setDisplayShowCustomEnabled(true)
            actionbarView = LayoutInflater.from(this).inflate(layoutId, null)
            val lParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            actionBar.setCustomView(actionbarView, lParams)
            initActionbarView(layoutId)
        }
    }

    private fun initActionbarView(layoutId: Int) {
        mTitleText = findViewById<View>(R.id.titleText) as TextView
        back = findViewById<View>(R.id.back) as ImageView
        rightImage = findViewById<View>(R.id.rightImage) as ImageView
        rightText = findViewById<View>(R.id.rightText) as TextView
        actionBarLine = findViewById(R.id.actionBarLine)
        if (back != null) back!!.setOnClickListener(clickListener)
        if (rightImage != null) rightImage!!.setOnClickListener(clickListener)
        if (rightText != null) rightText!!.setOnClickListener(clickListener)
    }

    fun findActionbarView(viewId: Int): View? {
        return if (actionbarView == null) {
            null
        } else {
            actionbarView!!.findViewById(viewId)
        }
    }


    fun titleGoneAll() {
        showBack(false, 0)
        showRightImage(false, 0)
        showRightText(false, 0)
        showTitle(false, 0)
    }

    fun showBack(visible: Boolean, imageResourceId: Int) {
        if (back == null) return
        if (visible) {
            back!!.visibility = View.VISIBLE
            back!!.isClickable = true
            if (imageResourceId != 0) {
                back!!.setImageResource(imageResourceId)
            }
        } else {
            back!!.visibility = View.INVISIBLE
            back!!.isClickable = false
        }
    }

    fun showActionBarLine(show: Boolean) {
        if (actionBarLine == null) return
        actionBarLine!!.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showRightImage(visible: Boolean, imageResourceId: Int) {
        if (rightImage == null) return
        if (visible) {
            rightImage!!.visibility = View.VISIBLE
            rightImage!!.isClickable = true
            if (imageResourceId != 0) {
                rightImage!!.setImageResource(imageResourceId)
            }
        } else {
            rightImage!!.visibility = View.INVISIBLE
            rightImage!!.isClickable = false
        }
    }

    fun showRightText(visible: Boolean, @StringRes text: Int) {
        if (rightText == null) return
        if (visible) {
            rightText!!.visibility = View.VISIBLE
            rightText!!.isClickable = true
            rightText!!.setText(text)
        } else {
            rightText!!.visibility = View.GONE
            rightText!!.isClickable = false
        }
    }

    fun showRightText(visible: Boolean, text: String) {
        if (rightText == null) return
        if (visible) {
            rightText!!.visibility = View.VISIBLE
            rightText!!.isClickable = true
            rightText!!.text = text
        } else {
            rightText!!.visibility = View.GONE
            rightText!!.isClickable = false
        }
    }

    fun showTitle(visible: Boolean, @StringRes text: Int) {
        if (mTitleText == null) return
        if (visible) {
            mTitleText!!.visibility = View.VISIBLE
            mTitleText!!.setText(text)
        } else {
            mTitleText!!.visibility = View.GONE
        }
    }

    fun showTitle(visible: Boolean, text: String?) {
        if (mTitleText == null) return
        if (visible) {
            mTitleText!!.visibility = View.VISIBLE
            mTitleText!!.text = text ?: ""
        } else {
            mTitleText!!.visibility = View.GONE
        }
    }

}

package com.shihang.kotlin.dialog

import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.Window
import com.shihang.kotlin.R
import com.shihang.kotlin.ui.BaseUI
import kotlinx.android.synthetic.main.dialog_menu.*


class MenuDialog(context: BaseUI) : Dialog(context, R.style.dialog_bottom) {

    private var menuClick: OnClickListener? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_menu, null)
        btnCancel.setOnClickListener{ dismiss() }
        menu1.setOnClickListener { menuClick(it) }
        menu2.setOnClickListener { menuClick(it) }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val lp = window!!.attributes
        lp.gravity = Gravity.BOTTOM
        onWindowAttributesChanged(lp)
        setCanceledOnTouchOutside(true)
        setContentView(view)
    }

    fun setMenuClick(menuClick: OnClickListener): MenuDialog {
        this.menuClick = menuClick
        return this
    }

    fun setMenu1Text(text: String?): MenuDialog {
        menu1.text = text ?: ""
        return this
    }

    fun setMenu2Text(text: String?): MenuDialog {
        menu2.text = text ?: ""
        return this
    }

    private fun menuClick(v:View){
        menuClick?.onClick(v)
        dismiss()
    }

}

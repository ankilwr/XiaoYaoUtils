package com.shihang.kotlin.dialog

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView

import com.shihang.kotlin.R


class MessageDialog(context: Activity) : Dialog(context, R.style.dialogBase) {

    private val textView: TextView?
    private var listener: View.OnClickListener? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_message, null)
        textView = view.findViewById<View>(R.id.tvMsg) as TextView
        view.findViewById<View>(R.id.btnCancel).setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnConfirm).setOnClickListener { v ->
            listener?.onClick(v)
            dismiss()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(false)
        setContentView(view)
    }

    fun setMessage(message: String): MessageDialog {
        textView?.text = message
        return this
    }

    fun setComfirmClick(listener: View.OnClickListener): MessageDialog {
        this.listener = listener
        return this
    }

}

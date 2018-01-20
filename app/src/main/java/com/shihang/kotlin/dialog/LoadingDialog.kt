package com.shihang.kotlin.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.shihang.kotlin.R
import com.shihang.kotlin.http.HttpManager
import okhttp3.Call


class LoadingDialog(context: Context) : Dialog(context, R.style.dialog_loading) {

    private var call: Call? = null
    var needCloseEnable: Boolean = true
    private val loadingAnim: AnimationDrawable
    private val tvTip: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        val image = view.findViewById<ImageView>(R.id.iv_main_loading)
        tvTip = view.findViewById<View>(R.id.tv_tips) as TextView
        tvTip.visibility = View.GONE
        loadingAnim = image.drawable as AnimationDrawable
        setOnCancelListener {
                HttpManager.cancelHttp(call)
                setCall(null)
        }
        setContentView(view)
    }

    fun setCall(call: Call?) {
        this.call = call
    }


    fun setMessage(message: String?) {
        if (message != null) {
            tvTip.text = message
            tvTip.visibility = View.VISIBLE
        } else {
            tvTip.visibility = View.GONE
        }
    }

    override fun cancel() {
        loadingAnim.stop()
        super.cancel()
    }

    override fun show() {
        loadingAnim.start()
        super.show()
    }

    override fun dismiss() {
        loadingAnim.stop()
        needCloseEnable = true
        super.dismiss()
    }

}

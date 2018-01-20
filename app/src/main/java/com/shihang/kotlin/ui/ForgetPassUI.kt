package com.shihang.kotlin.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.shihang.kotlin.R
import com.shihang.kotlin.extends.showToast
import kotlinx.android.synthetic.main.ui_forget_pass.*


class ForgetPassUI : ActionBarUI() {

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_forget_pass)
        showBack(true, 0)
        showTitle(true, R.string.title_forget_pass)
        btnCode.setOnClickListener{ onClick(it)  }
        btnNext.setOnClickListener{ onClick(it)  }
    }

    private fun codeBtnCount() {
        timer = object : CountDownTimer((60 * 1000).toLong(), 1000) {
            override fun onTick(l: Long) {
                btnCode.isEnabled = false
                btnCode.text = String.format(getString(R.string.btn_code_count), l / 1000)  //设置倒计时时间
            }

            override fun onFinish() {
                btnCode.isEnabled = true
                btnCode.isClickable = true
                btnCode.text = getString(R.string.btn_get_code)
                timer = null
            }
        }.start()
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.btnCode -> codeBtnCount()
            R.id.btnNext -> showToast("下一步")
        }
    }
}

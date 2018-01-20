package com.shihang.kotlin.fragment

import android.view.View
import com.shihang.kotlin.R
import com.shihang.kotlin.extends.showToast
import com.shihang.kotlin.view.HomeTabButton
import com.shihang.kotlin.view.HomeTabGroup.OnCheckedChangeListener
import kotlinx.android.synthetic.main.fm_mine.*

class MineFm : TitleFm(), OnCheckedChangeListener {

    override val layout: Int = R.layout.fm_mine

    override fun onSelect(button: HomeTabButton, index: Int) {

    }

    override fun onUnSelect(button: HomeTabButton, index: Int) {

    }

    override fun onReSelect(button: HomeTabButton, index: Int) {

    }

    override fun initViews() {
        super.initViews()
        showTitle(true, "我的")
        btnBasic.setOnClickListener{ onClick(it) }
        btnDoctor.setOnClickListener{ onClick(it) }
        btnModel.setOnClickListener{ onClick(it) }
        btnRecord.setOnClickListener{ onClick(it) }
        btnTixian.setOnClickListener{ onClick(it) }
        btnSetting.setOnClickListener{ onClick(it) }
    }

    override fun rightClick(v:View){
        context.showToast("消息")
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.btnBasic -> context.showToast("基本资料")
            R.id.btnDoctor -> context.showToast("医生资料")
            R.id.btnModel -> context.showToast("处方模版")
            R.id.btnRecord -> context.showToast("开方记录")
            R.id.btnTixian -> context.showToast("提现")
            R.id.btnSetting -> context.showToast("设置")
            R.id.rightImage -> context.showToast("消息")
        }
    }
}

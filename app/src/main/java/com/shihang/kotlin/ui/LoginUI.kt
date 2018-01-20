package com.shihang.kotlin.ui

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import com.shihang.kotlin.R
import com.shihang.kotlin.http.HttpCallBack
import com.shihang.kotlin.http.HttpManager
import com.shihang.kotlin.utils.CommonUtil
import kotlinx.android.synthetic.main.ui_login.*


class LoginUI : ActionBarUI(), OnCheckedChangeListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_login)
        showTitle(true, R.string.title_login)
        btnLogin.setOnClickListener{ onClick(it) }
        btnForgetPass.setOnClickListener{ onClick(it) }
        checkAuto.setOnCheckedChangeListener(this)
        checkPass.setOnCheckedChangeListener(this)
    }


    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.checkAuto -> {

            }
            R.id.checkPass -> {
                if (isChecked) {
                    etPass!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    etPass!!.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                CommonUtil.editSetLastLength(etPass)
            }
        }
    }


    private fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                HttpManager.httpDialogRequest(showLoadingDialog(true), this, "http://www.baidu.com", null, object:HttpCallBack{
                    override fun result(success: Boolean, result: String) {
                        openUI(HomeUI::class.java)
                    }
                })
            }
            R.id.btnForgetPass -> openUI(ForgetPassUI::class.java)
        }
    }


}

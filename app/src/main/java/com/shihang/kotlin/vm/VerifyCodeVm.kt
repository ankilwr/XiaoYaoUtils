package com.shihang.kotlin.vm

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mellivora.base.expansion.showToast
import com.mellivora.base.vm.LoadingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerifyCodeVm: LoadingViewModel() {

    val phoneData = MutableLiveData<String>()
    val timeTickData = MutableLiveData<Long>()
    private var timerTick: CountDownTimer? = null

    fun getVerifyCode(){
        val phone = phoneData.value?.trim()
        if(phone.isNullOrEmpty()){
            showToast("手机号不能为空")
            return
        }
        viewModelScope.launch {
            showLoadingDialog()
            withContext(Dispatchers.IO){
                //休眠2s，模拟加载接口
                delay(2000L)
            }
            showToast("获取验证码成功")
            dismissLoadingDialog()
            //启动计时器
            timerTick?.cancel()
            timerTick = object: CountDownTimer(60_000L, 1000L){
                override fun onTick(millisUntilFinished: Long) {
                    timeTickData.value = millisUntilFinished
                }
                override fun onFinish() {
                    timeTickData.value = 0L
                }
            }
            timerTick?.start()
        }
    }

    override fun onCleared() {
        timerTick?.cancel()
    }

}
package com.shihang.kotlin.extends

import android.content.Context
import android.support.annotation.StringRes
import com.shihang.kotlin.bean.BaseBean
import com.shihang.kotlin.utils.TShow


//------------------------------ Any扩展 -----------------------------
fun Any?.getLogTag(): String {
    if (this == null) return "null"
    return this.javaClass.simpleName
}

fun Any?.getFailureMsg(result: String?, bean: BaseBean?, def: String? = "服务器正忙，请稍后再试"): String {
    return when {
        bean != null -> bean.getMessage()
        result == null -> def ?: "服务器正忙，请稍后再试"
        result.contains("java.net.UnknownHostException") -> "请检查网络是否连接"
        result.contains("java.net.SocketTimeoutException") -> "请求连接超时"
        result.contains("java.net.SocketException: Socket closed")
                || result.contains("java.io.IOException: Canceled")  -> "请求取消"
        else -> def ?: "服务器正忙，请稍后再试"
    }
}




//------------------------------ Context扩展出toast方法 -----------------------------
fun Context?.showToast(text: String?) {
    if(this != null) TShow.show(this, text)
}

fun Context?.showToast(@StringRes strId: Int) {
    if(this != null) TShow.show(this, strId)
}

fun Context?.showFailureTost(result: String, bean: BaseBean?, def: String?) {
    this?.showToast(this.getFailureMsg(result, bean, def))
}









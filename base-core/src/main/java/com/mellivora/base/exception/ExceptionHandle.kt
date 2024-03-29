package com.mellivora.base.exception

import android.util.MalformedJsonException
import com.google.gson.JsonParseException
import com.mellivora.base.core.R
import com.mellivora.base.expansion.getResString
import com.mellivora.base.utils.LogUtils
import kotlinx.coroutines.CancellationException
import org.json.JSONException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

class ExceptionHandle(
    var errorCode: Int = ErrorStatus.UNKNOWN_ERROR,
    var errorMsg: String = getResString(R.string.base_http_server_error)) {
}

fun Throwable?.parse(): ExceptionHandle {
    LogUtils.print2ConsoleError("Throwable.parse():${this?.stackTraceToString()}")
    val errorInfo = ExceptionHandle()
    when (this) {
        is DataCheckException -> {
            errorInfo.errorMsg = if (message.isNullOrEmpty()) getResString(R.string.base_http_server_error) else message!!
            errorInfo.errorCode = ErrorStatus.API_ERROR
        }
        is SocketTimeoutException, is ConnectException, is UnknownHostException -> {
            errorInfo.errorMsg = getResString(R.string.base_http_net_work_error)
            errorInfo.errorCode = ErrorStatus.NETWORK_ERROR
        }
        is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
            errorInfo.errorMsg = getResString(R.string.base_http_server_error)
            errorInfo.errorCode = ErrorStatus.API_ERROR
        }
        is CancellationException -> {
            errorInfo.errorMsg = "Task Canceled"
            errorInfo.errorCode = ErrorStatus.CANCEL
        }
        //这两个异常辈分比较大，判断放在后面
        is SocketException, is IOException -> {
            //java.net.SocketException: Socket is closed
            //java.net.SocketException: Socket closed
            //java.io.IOException: Canceled
            message?.let {
                if(it.contains("Socket is closed") || it.contains("Socket closed") || it.contains("Canceled")) {
                    errorInfo.errorMsg = "Http Canceled"
                    errorInfo.errorCode = ErrorStatus.CANCEL
                }
            }
        }
        else -> {
            errorInfo.errorMsg = getResString(R.string.base_http_unknown_error)
            errorInfo.errorCode = ErrorStatus.UNKNOWN_ERROR
        }
    }
    return errorInfo
}

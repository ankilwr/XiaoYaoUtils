package com.shihang.kotlin.http

import android.os.Handler
import android.util.SparseArray
import com.shihang.kotlin.dialog.LoadingDialog
import com.shihang.kotlin.extends.getLogTag
import com.shihang.kotlin.utils.LogUtil
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object HttpManager {

    private class HttpCall constructor(val tag: Any, val call: Call) {

        override fun hashCode(): Int {
            return call.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as HttpCall
            if (tag != other.tag) return false
            if (call != other.call) return false
            return true
        }
    }

    private val logTag = "HttpManager"
    private val httpCalls = SparseArray<HttpCall>()
    private val handler: Handler = Handler()


    private fun getRequest(tag: Any, url: String, params: Map<String, Any>?): Request {
        var logUrl = "请求连接:" + url + "\n"
        val builder = Request.Builder().url(url)

        //添加请求参数
        if (params != null) {
            val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            for ((paramKey, param) in params) {
                logUrl += "param:$paramKey = "
                if (param is File || param is Array<*>) {
                    if (param is File) {
                        //单文件上传
                        //RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                        requestBody.addFormDataPart(paramKey, param.name, RequestBody.create(MediaType.parse("application/json; charset=utf-8"), param))
                        logUrl += "file(${param.absolutePath}):type(image/*)\n"
                    } else {
                        //多文件上传
                        for (file in param as Array<*>) {
                            if (file !is File) continue
                            requestBody.addFormDataPart(paramKey, file.name, RequestBody.create(MediaType.parse("application/json; charset=utf-8"), file))
                            logUrl += "file(${file.absolutePath}):type(image/*)\n"
                        }
                    }
                } else {
                    //普通的参数(字符串)
                    requestBody.addFormDataPart(paramKey, param.toString())
                    logUrl += param.toString() + "\n"
                }
            }
            builder.post(requestBody.build())
        } else {
            builder.get()
        }
        LogUtil.i(tag.getLogTag(), logUrl)
        return builder.build()
    }

    private fun getCallBack(tag: Any, loading: LoadingDialog?, callback: HttpCallBack): Callback {
        return object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LogUtil.i(tag.toString(), "请求失败: ${call.request().url()}\n错误: $e")
                handler.post {
                    if (!e.toString().contains("Socket closed") && !e.toString().contains("Canceled")) {
                        //不属于取消错误，通知错误回调(用于弹出错误提示)
                        callback.result(false, e.toString())
                    }
                    if (loading != null && loading.isShowing) loading.dismiss()//失败的时候关闭加载框
                    LogUtil.info(logTag, "removeHttp:${call.hashCode()} --- error: $e")
                    httpCalls.remove(call.hashCode())
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body()!!.string()
                LogUtil.info(tag.getLogTag(), "请求成功: ${call.request().url()}\n$result")
                handler.post {
                    callback.result(true, result)
                    if (loading != null && loading.needCloseEnable && loading.isShowing) loading.dismiss()
                    LogUtil.info(logTag, "removeHttp:${call.hashCode()} --- 请求完成(finish)")
                    httpCalls.remove(call.hashCode())
                }
            }
        }
    }



    @Synchronized fun cancelHttpForTag(tag: Any) {
        (0 until httpCalls.size())
                .map { httpCalls.valueAt(it) }
                .filter { it.tag == tag }
                .forEach {
                    LogUtil.info(logTag, "cancelHttp:${it.call.hashCode()}")
                    if(!it.call.isCanceled) it.call.cancel()
                }
    }

    @Synchronized fun cancelHttp(call: Call?) {
        call ?: return
        LogUtil.info(logTag, "cancelHttp:${call.hashCode()}")
        val cancelCall: Call = httpCalls.get(call.hashCode()).call
        if(!cancelCall.isCanceled) cancelCall.cancel()
    }

    fun httpRequest(tag: Any, url: String, params: Map<String, Any>?, callback: HttpCallBack): Call {
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params);
        //Request request = new Request.Builder().url(url).post(requestBody).build();
        return httpDialogRequest(null, tag, url, params, callback)
    }

    fun httpDialogRequest(loading: LoadingDialog?, tag: Any, url: String, params: Map<String, Any>?, callback: HttpCallBack, requestTime: Long = 10_000): Call {
        val client = OkHttpClient.Builder()
                .readTimeout(requestTime, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(requestTime, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(requestTime, TimeUnit.SECONDS)//设置连接超时时间
                .build()
        val call = client.newCall(getRequest(tag, url, params))
        loading?.setCall(call)
        httpCalls.put(call.hashCode(), HttpCall(tag, call))
        call.enqueue(getCallBack(tag, loading, callback))
        LogUtil.info(logTag, "sendHttp:${call.hashCode()}")
        return call
    }






}


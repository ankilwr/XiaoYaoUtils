package com.mellivora.base.http


import android.util.Log
import com.google.gson.reflect.TypeToken
import com.mellivora.base.coroutine.safeResume
import com.mellivora.base.coroutine.safeResumeWithException
import com.mellivora.base.expansion.jsonConvertBeanThrow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.HttpException

/**
 * 执行Call<>, 并返回Call里的泛型实体
 */
suspend inline fun <reified R> Call<R>.suspendExecute() = executeConvert<R>()

/**
 * 执行Call<String>，并将结果转换为指定类型实体返回
 */
suspend inline fun <reified R> Call<*>.executeConvert() = suspendCancellableCoroutine {
    it.invokeOnCancellation { e ->
        //协程任务取消, 同时取消HTTP请求
        this.cancel()
    }
    //执行HTTP请求
    val response = this.execute()
    if(it.isCancelled){
        return@suspendCancellableCoroutine
    }
    if (response.isSuccessful) {
        val responseData = response.body()
        val typeToken = object: TypeToken<R>(){}
        if(responseData is String && responseData.javaClass != typeToken.rawType){
            try {
                val data = responseData.jsonConvertBeanThrow(typeToken)
                it.safeResume(data)
            }catch (e: Throwable){
                it.safeResumeWithException(e)
            }
        }else{
            it.safeResume(responseData as R)
        }
    } else {
        Log.e("测试测试", "http通信失败: $response")
        it.safeResumeWithException(HttpException(response))
    }
}


package com.mellivora.base.http


import com.mellivora.base.coroutine.safeResume
import com.mellivora.base.coroutine.safeResumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.HttpException

suspend inline fun <T> Call<T>.executeExt() = suspendCancellableCoroutine {
    //取消请求
    it.invokeOnCancellation { e ->
        this.cancel()
    }
    val response = this.execute()
    if(it.isCancelled){
        return@suspendCancellableCoroutine
    }
    if (response.isSuccessful) {
        it.safeResume(response.body())
    } else {
        it.safeResumeWithException(HttpException(response))
        //it.safeResumeWithException(response?.exception ?: UnknownError())
    }
//    else if(response.raw() != null){
//        it.safeResumeWithException(HttpException(response))
//    }
}

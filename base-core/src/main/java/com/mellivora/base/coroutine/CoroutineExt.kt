package com.mellivora.base.coroutine

import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

inline fun <T> CancellableContinuation<T>.safeResume(value: T, onExceptionCalled: () -> Unit = {}) {
    if (isActive){
        resume(value)
    }else{
        onExceptionCalled()
    }
}

inline fun <T> CancellableContinuation<T>.safeResumeWithException(exception: Throwable, onExceptionCalled: () -> Unit= {}) {
    if (isActive){
        resumeWithException(exception)
    }else{
        onExceptionCalled()
    }
}
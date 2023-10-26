package com.mellivora.base.coroutine

import androidx.core.util.Consumer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

sealed class IResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : IResult<T>()
    data class Error(val exception: Exception) : IResult<Nothing>()

    override fun toString(): String {
        return when(this){
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

inline fun <T: Any> IResult<T>.onCheckSuccess(crossinline block: (T) -> Unit): Throwable?{
    return onCheckSuccess(Consumer<T> { block.invoke(it) })
}


fun <T: Any> IResult<T>.onCheckSuccess(onSuccess: Consumer<T>): Throwable?{
    when(this){
        is IResult.Success -> {
            try {
                onSuccess.accept(data)
            }catch (e: Exception){
                return e
            }
        }
        is IResult.Error -> {
            return exception
        }
    }
    return null
}

inline fun Throwable?.onCheckError(crossinline block: (Throwable) -> Unit){
    return onCheckError (Consumer<Throwable> { block.invoke(it) })
}


fun Throwable?.onCheckError(onError: Consumer<Throwable>){
    if(this == null) return
    onError.accept(this)
}


fun <T: Any> IResult<T>.checkResult(onSuccess: Consumer<T>, onError: Consumer<Throwable>){
    when(this){
        is IResult.Success -> {
            try {
                onSuccess.accept(data)
            }catch (e: Exception){
                onError.accept(e)
            }
        }
        is IResult.Error -> {
            onError.accept(exception)
        }
    }
}


/**
 * 运行在IO子协程的异常处理
 */
suspend fun <T : Any> withIOResult(
    dispatchers: CoroutineDispatcher = Dispatchers.IO,
    block: suspend CoroutineScope.() -> T
): IResult<T> {
    return try {
        IResult.Success(withContext(dispatchers) { block() })
    } catch (e: Exception) {
        IResult.Error(e)
    }
}


/**
 * 并发运行的IO子协程操作(已捕捉异常)
 */
inline fun <reified T : Any> CoroutineScope.asyncIOResult(
    dispatchers: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend CoroutineScope.() -> T
): Deferred<IResult<T>> {
    return async(dispatchers){
        try {
            IResult.Success(block())
        } catch (e: java.lang.Exception) {
            IResult.Error(e)
        }
    }
}


fun <T : Any> IResult<T>.getData(): T{
    when(this){
        is IResult.Success -> {
            return data
        }
        is IResult.Error -> {
            throw exception
        }
    }
}

fun <T : Any> IResult<T>.getDataOrNull(): T?{
    return when(this){
        is IResult.Success -> {
            data
        }
        is IResult.Error -> {
            null
        }
    }
}
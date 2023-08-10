package com.mellivora.data.repository.http.interceptor

import com.mellivora.data.repository.http.cache.CacheManager
import com.mellivora.data.repository.http.cache.CacheMode
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * 根据缓存策略，保存请求结果(获取到网络结果后保存)
 */
class CacheModeResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheMode = request.header(CacheManager.CACHE_MODE)
        val cacheValue = request.header(CacheManager.CACHE_VALUE)

        //缓存策略
        val cacheControl = getCacheControl(cacheMode, cacheValue)

        //请求结果
        val response = if(cacheControl == null){
            chain.proceed(request)
        }else {
            //请求头移除缓存策略，不需要发送到服务端
            val newRequest = request.newBuilder()
                .removeHeader(CacheManager.CACHE_MODE)
                .removeHeader(CacheManager.CACHE_VALUE)
                .build()
            chain.proceed(newRequest)
        }

        //响应体写入缓存参数
        if(cacheControl != null){
            return response.newBuilder()
                .header("cache-control", cacheControl.toString())
                .build()
        }
        return response
    }

}

/**
 * 请求缓存策略(每次发起请求都需要判断，有可能动态调整策略)
 */
class CacheModeRequestInterceptor: Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheMode = request.header(CacheManager.CACHE_MODE)
        val cacheValue = request.header(CacheManager.CACHE_VALUE)

        val cacheControl = getCacheControl(cacheMode, cacheValue)

        val response = if(cacheControl != null){
            val newRequest = request.newBuilder()
                .header("cache-control", cacheControl.toString())
                .build()
            chain.proceed(newRequest)
        }else{
            chain.proceed(request)
        }
        return response
    }

}

fun getCacheControl(cacheMode: String?, cacheValue: String?): CacheControl?{
    return when(cacheMode){
        //不使用缓存
        CacheMode.MODE_NO_CACHE -> CacheControl.FORCE_NETWORK
        //缓存今日有效
        CacheMode.MODE_CACHE_TODAY -> {
            val currentDate = Calendar.getInstance()
            val targetDate = Calendar.getInstance().apply {
                clear()
                set(currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DAY_OF_MONTH])
                add(Calendar.DAY_OF_MONTH, 1)
            }
            val cacheTime = targetDate.timeInMillis - currentDate.timeInMillis
            CacheControl.Builder()
                .maxAge((cacheTime / 1000L).toInt(), TimeUnit.SECONDS)
                .build()
        }
        //指定缓存有效期
        CacheMode.MODE_CACHE_EXPIRATION -> {
            val cacheTime = cacheValue?.toIntOrNull() ?: return null
            CacheControl.Builder()
                .maxAge(cacheTime, TimeUnit.SECONDS)
                .build()
        }
        else -> null
    }
}
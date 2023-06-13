package com.mellivora.base.http.interceptor

import com.mellivora.base.http.cache.CacheManager
import com.mellivora.base.http.cache.CacheMode
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * 根据缓存模式返回缓存
 */
class CacheModeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheMode = request.header(CacheManager.CACHE_MODE)
        val cacheValue = request.header(CacheManager.CACHE_VALUE)
        val newRequest = if (cacheMode != null || cacheValue != null){
            request.newBuilder()
                .removeHeader(CacheManager.CACHE_MODE)
                .removeHeader(CacheManager.CACHE_VALUE)
                .build()
        }else{
            request
        }
        val response = chain.proceed(newRequest)

        //用户端未指定需要的缓存模式, 直接返回response
        if(cacheMode == null){
            return response
        }
        //用户端了有效期的缓存模式, 但是未设置有效期的值，直接返回response
        if(cacheMode == CacheMode.MODE_CACHE_EXPIRATION && cacheValue == null){
            return response
        }
        val cacheControl = when(cacheMode){
            CacheMode.MODE_NO_CACHE -> CacheControl.FORCE_NETWORK
            CacheMode.MODE_CACHE_ONLY -> CacheControl.FORCE_CACHE
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
            CacheMode.MODE_CACHE_EXPIRATION -> {
                val cacheTime = cacheValue?.toIntOrNull()
                if(cacheTime != null){
                    CacheControl.Builder()
                        .maxAge(cacheTime, TimeUnit.SECONDS)
                        .build()
                }else{
                    null
                }
            }
            else -> null
        }
        //响应体写入缓存参数
        cacheControl?.let {
            return response.newBuilder()
                .header("cache-control", it.toString())
                .build()
        }
        return response
    }

}
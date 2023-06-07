package com.mellivora.base.http.interceptor

import com.mellivora.base.http.cache.CacheManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * 根据缓存模式返回缓存
 */
class CacheModeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val cacheMode = request.header(CacheManager.CACHE_MODE_KEY)
        if (cacheMode == null || cacheMode == CacheManager.CACHE_MODE_NO_CACHE){
            return response
        }

        when(cacheMode){

        }

        val cacheControl = CacheControl.Builder()
            .maxAge(10, TimeUnit.MINUTES)
            .build()

        //响应体写入缓存参数
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }

}
package com.mellivora.data.repository.http.interceptor

import com.mellivora.data.repository.http.cache.CacheMode
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 根据缓存策略，保存请求结果(获取到网络结果后保存)
 */
class CacheNetworkResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheValue = parseCache(request.header(CacheMode.MODE)) ?: return chain.proceed(request)
        //请求头移除缓存策略，不需要发送到服务端
        val newRequest = request.newBuilder()
            .removeHeader(CacheMode.MODE)
            .build()
        //响应体写入缓存协议
        return chain.proceed(newRequest).newBuilder()
            .header("cache-control", cacheValue)
            .build()
    }
}

/**
 * 请求缓存策略(每次发起请求都需要判断，有可能动态调整策略)
 */
class CacheRequestInterceptor: Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheValue = parseCache(request.header(CacheMode.MODE)) ?: return chain.proceed(request)
        val newRequest = request.newBuilder()
            .header("cache-control", cacheValue)
            .build()
        return chain.proceed(newRequest)
    }
}

/**
 * 解析缓存的模式
 */
fun parseCache(cacheValue: String?): String?{
    return when(cacheValue){
        CacheMode.Today.cacheValue -> CacheMode.Today.getCacheTime()
        else -> cacheValue
    }
}
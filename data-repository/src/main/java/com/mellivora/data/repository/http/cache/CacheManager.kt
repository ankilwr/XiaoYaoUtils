package com.mellivora.data.repository.http.cache


object CacheMode{

    /**
     * 无缓存
     */
    const val MODE_NO_CACHE = "mode-no-cache"

    /**
     * 今日有效
     */
    const val MODE_CACHE_TODAY = "mode-today"

    /**
     * 有效期(单位：秒)
     */
    const val MODE_CACHE_EXPIRATION = "mode-cache-expiration"

}

object CacheManager {
    const val CACHE_MODE = "Cache-Mode"
    const val CACHE_VALUE = "Cache-Value"
}
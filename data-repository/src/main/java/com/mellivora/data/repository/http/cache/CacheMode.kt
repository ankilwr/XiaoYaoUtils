package com.mellivora.data.repository.http.cache

import okhttp3.CacheControl
import java.util.Calendar
import java.util.concurrent.TimeUnit


sealed class CacheMode(val cacheValue: String){

    override fun toString(): String {
        return cacheValue
    }

    companion object{
        const val MODE = "CacheMode"
    }

    /**
     * 不使用缓存
     */
    object NoCache: CacheMode(CacheControl.FORCE_NETWORK.toString())

    /**
     * 今日有效
     */
    object Today: CacheMode("Today"){
        fun getCacheTime(): String{
            val nowDate = Calendar.getInstance()
            val targetDate = Calendar.getInstance().apply {
                clear()
                set(nowDate[Calendar.YEAR], nowDate[Calendar.MONTH], nowDate[Calendar.DAY_OF_MONTH])
                add(Calendar.DAY_OF_MONTH, 1)
            }
            val cacheTime = targetDate.timeInMillis - nowDate.timeInMillis
            return CacheControl.Builder()
                .maxAge((cacheTime / 1000L).toInt(), TimeUnit.SECONDS)
                .build()
                .toString()
        }
    }

    /**
     * 有效期(默认单位：Hours)
     */
    data class CacheTime(val age: Int, val unit: TimeUnit = TimeUnit.HOURS): CacheMode(
        CacheControl.Builder()
            .maxAge(age, unit)
            .build()
            .toString()
    )
}
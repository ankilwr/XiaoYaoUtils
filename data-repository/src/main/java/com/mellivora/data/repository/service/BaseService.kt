package com.mellivora.data.repository.service

import com.mellivora.base.utils.LogUtils
import com.mellivora.data.repository.http.interceptor.CacheModeRequestInterceptor
import com.mellivora.data.repository.http.interceptor.CacheModeResponseInterceptor
import com.mellivora.base.utils.Utils
import com.mellivora.data.repository.http.converter.CopyGsonConverterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit


object BaseService {

    /**
     * 如果需要在其他模块拓展服务，可将改client注入
     */
    val okHttpClient by lazy {
        val logInterceptor = HttpLoggingInterceptor {
            LogUtils.iTag("HttpInterceptor", it)
        }
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient().newBuilder()
            .cache(Cache(File(Utils.getApp().cacheDir, "http-cache"), 50L * 1024L * 1024L)) // 50 MiB
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addNetworkInterceptor(CacheModeResponseInterceptor())
            .addInterceptor(CacheModeRequestInterceptor())
            .addInterceptor(logInterceptor)
            .build()
    }

    /**
     * GitHub服务通信
     */
    val githubService: GithubService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(CopyGsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(GithubService::class.java)
    }

}
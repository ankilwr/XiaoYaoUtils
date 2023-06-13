package com.mellivora.base.repository

import android.util.Log
import com.mellivora.base.http.converter.CopyGsonConverterFactory
import com.mellivora.base.http.interceptor.CacheModeInterceptor
import com.mellivora.base.utils.Utils
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit


interface BaseService {

    companion object{
        private val okHttpClient by lazy {
            val logInterceptor = HttpLoggingInterceptor {
                Log.i("测试测试", it)
            }
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            OkHttpClient().newBuilder()
                .cache(Cache(File(Utils.getApp().cacheDir, "http-cache"), 50L * 1024L * 1024L)) // 50 MiB
                .addNetworkInterceptor(CacheModeInterceptor())
                .connectTimeout(15L, TimeUnit.SECONDS)
                .readTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS)
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

}
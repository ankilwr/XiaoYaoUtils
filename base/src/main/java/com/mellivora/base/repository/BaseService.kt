package com.mellivora.base.repository

import com.mellivora.base.utils.Utils
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


interface BaseService {

    companion object{
        private val okHttpClient by lazy {
            OkHttpClient().newBuilder()
                .cache(Cache(File(Utils.getApp().cacheDir, "http-cache"), 50L * 1024L * 1024L)) // 50 MiB
                //.addNetworkInterceptor(CacheModeInterceptor())
                .build()
        }

        val githubService: GithubService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            retrofit.create(GithubService::class.java)
        }
    }

}

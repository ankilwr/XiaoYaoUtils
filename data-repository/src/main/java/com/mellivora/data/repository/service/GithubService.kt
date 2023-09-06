package com.mellivora.data.repository.service

import com.mellivora.data.repository.http.cache.CacheManager
import com.mellivora.data.repository.http.cache.CacheMode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GithubService{

//    //缓存有效期30秒
//    @Headers(
//        "${CacheManager.CACHE_MODE}:${CacheMode.MODE_CACHE_EXPIRATION}",
//        "${CacheManager.CACHE_VALUE}:30"
//    )
//  //缓存有效期当天有效
    @Headers("${CacheManager.CACHE_MODE}:${CacheMode.MODE_CACHE_TODAY}")
    @GET("users/{user}")
    fun getGithubUserInfo(
        @Path("user") user: String?
    ): Call<String>


    @GET("users/{user}/repos")
    fun getGithubRepositoryList(
        @Path("user") user: String?
    ): Call<String>

}
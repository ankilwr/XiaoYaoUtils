package com.mellivora.data.repository.service

import com.mellivora.data.repository.http.cache.CacheMode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GithubService{

    @GET("users/{user}")
    fun getGithubUserInfo(
        @Path("user") user: String?
    ): Call<String>


    @GET("users/{user}/repos")
    fun getGithubRepositoryList(
        @Header(CacheMode.MODE) cache: CacheMode,
        @Path("user") user: String?
    ): Call<String>

}
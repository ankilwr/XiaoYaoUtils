package com.mellivora.base.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService{

    @GET("users/{user}")
    fun getGithubUserInfo(
        @Path("user") user: String?
    ): Call<String>


    @GET("users/{user}/repos")
    fun getGithubRepositoryList(
        @Path("user") user: String?
    ): Call<String>

}
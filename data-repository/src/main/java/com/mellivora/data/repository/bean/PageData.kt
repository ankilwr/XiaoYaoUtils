package com.mellivora.data.repository.bean

import com.google.gson.annotations.SerializedName

data class PageData(
    @SerializedName("total_count")
    val totalCount: Int = 0,
    @SerializedName("total_page")
    val totalPage: Int = 0,
    @SerializedName("current_page")
    val currentPage: Int = 0,
    @SerializedName("page_size")
    val pageSize: Int = 0
)
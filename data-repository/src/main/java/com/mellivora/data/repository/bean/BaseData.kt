package com.mellivora.data.repository.bean

import com.google.gson.annotations.SerializedName


class BaseData<T> {
    val code: String? = null
    val msg: String? = null
    val data: T? = null
    val page: PageData? = null

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

    /**
     * @return 是否有下一页数据
     */
    fun hasMore(): Boolean{
        page ?: return false
        return page.currentPage < page.totalPage
    }
}
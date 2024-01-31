package com.mellivora.data.repository.bean


class BaseData<T> {
    val code: String? = null
    val msg: String? = null
    val data: T? = null
    val page: PageData? = null

    /**
     * @return 是否有下一页数据
     */
    fun hasMore(): Boolean{
        page ?: return false
        return page.currentPage < page.totalPage
    }
}
package com.mellivora.data.repository.bean


class BaseData<T> {

    val code: String? = null
    val msg: String? = null
    val data: T? = null

    fun httpCheck(): Boolean{
        return "200" == code
    }

}
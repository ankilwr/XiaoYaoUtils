package com.mellivora.base.exception

class DataCheckException(
    val error: String = "",
    val code: Int? = null
) : Exception(error)

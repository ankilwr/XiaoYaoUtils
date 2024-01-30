package com.mellivora.base.exception

class DataCheckException(
    val error: String = "",
    val code: String? = null
) : Exception(error)

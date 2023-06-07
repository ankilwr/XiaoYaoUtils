package com.mellivora.base.exception

object ErrorStatus {
    /**
     * 任务取消
     */
    const val CANCEL = 999

    /**
     * 响应成功
     */
    const val SUCCESS = 0

    /**
     * 未知错误
     */
    const val UNKNOWN_ERROR = 1002

    /**
     * 网络连接超时
     */
    const val NETWORK_ERROR = 1003

    /**
     * 服务器内部错误
     */
    const val SERVER_ERROR = 1004

    /**
     * API解析异常（或者第三方数据结构更改）等其他异常
     */
    const val API_ERROR = 1005

    /**
     * 返回正常code，但返回了不能为空的Data
     */
    const val API_DATA_ERROR = 1006


}
package com.shihang.kotlin.bean

open class BaseBean {

    private var status: String? = null
    private var message: String? = null

    private var page: Page? = null

    class Page{
        var total: String? = null
        var page: String? = null
        var pageSize: String? = null
    }

    fun httpCheck(): Boolean {
        //== 比较值， ===比较地址
        return "1" == status
    }

    fun getMessage(): String {
        return when {
            message != null -> message!!
            httpCheck() -> "成功"
            else -> "失败"
        }
    }

    fun hasNext(): Boolean{
        if(page != null){
            try {
                val total = page!!.total!!.toInt()
                val pageSize = page!!.pageSize!!.toInt()
                val pageNum = page!!.page!!.toInt()
                return total > pageNum * pageSize
            }catch (e: Exception){

            }
        }
        return false
    }

}

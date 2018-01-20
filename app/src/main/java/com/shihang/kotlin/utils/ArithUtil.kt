package com.shihang.kotlin.utils

import java.math.BigDecimal
import java.text.DecimalFormat

object ArithUtil {

    // 默认除法运算精度
    private val DEF_DIV_SCALE = 10


    /**
     * 提供精确的加法运算
     */
    fun add(v1: Double, v2: Double, scale: Int = -1): String {
        return add(java.lang.Double.toString(v1), java.lang.Double.toString(v2), scale)
    }

    fun add(v1: String, v2: String, scale: Int = -1): String {
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return round(b1.add(b2).toDouble(), scale)
    }


    /**
     * 提供精确的减法运算
     */
    fun sub(v1: Double, v2: Double, scale: Int = -1): String {
        return add(java.lang.Double.toString(v1), java.lang.Double.toString(v2), scale)
    }

    fun sub(v1: String, v2: String, scale: Int = -1): String {
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return round(b1.subtract(b2).toDouble(), scale)
    }



    /**
     * 提供精确的乘法运算
     */
    fun mul(v1: Double, v2: Double, scale: Int = -1): String {
        return mul(java.lang.Double.toString(v1),java.lang.Double.toString(v2), scale)
    }

    fun mul(v1: String, v2: String, scale: Int = -1): String {
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return round(b1.multiply(b2).toDouble(), scale)
    }




    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     */
    @JvmOverloads
    fun div(v1: Double, v2: Double, scale: Int = DEF_DIV_SCALE): Double {
        if (scale < 0) {
            throw IllegalArgumentException("精度错误,传入的精度必须为int型参数且必须大于等于0")
        }
        val b1 = BigDecimal(java.lang.Double.toString(v1))
        val b2 = BigDecimal(java.lang.Double.toString(v2))
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }


    fun round(v1: Double, scale: Int): String {
        var scale = scale
        if (scale <= 0) {
            scale = 1
        }
        val buString = StringBuilder("0.")
        for (i in 0 until scale) {
            buString.append("0")
        }
        val df = DecimalFormat(buString.toString())
        //不能返回double,Double.parseDouble("2.00")转换成2.0
        //return Double.parseDouble(df.format(v1));
        return df.format(v1)
    }
}

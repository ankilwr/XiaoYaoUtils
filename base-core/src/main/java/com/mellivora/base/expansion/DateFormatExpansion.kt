package com.mellivora.base.expansion

import java.lang.Long.parseLong
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期转字符串
 */
fun Long.timeInMillisFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return Date(this).dateFormat(format)
}

/**
 * 日期转字符串
 */
fun String.timeInMillisFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return Date(parseLong(this)).dateFormat(format)
}

/**
 * 日期转字符串
 */
fun Calendar.calendarFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return this.time.dateFormat(format)
}

/**
 * 日期转字符串
 */
fun Date.dateFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}
/**
 * 字符串日期转Date
 * 例:1900-12-12 23:59:59
 */
fun String.toDate(format: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }
}

fun Long.timeInMillisTickFormat(): String?{
    return try {
        val format = if(this >= 3600_000L) "HH:mm:ss" else "mm:ss"
        SimpleDateFormat(format, Locale.getDefault()).format(this)
    } catch (e: Exception) {
        null
    }
}

fun Int.secondTickFormat(): String?{
    return (this * 1000L).timeInMillisTickFormat()
}

fun String.toCalendar(format: String = "yyyy-MM-dd HH:mm:ss"): Calendar? {
    return try {
        val date = SimpleDateFormat(format, Locale.getDefault()).parse(this)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar
    } catch (e: Exception) {
        null
    }
}

fun Calendar.clearHour(): Calendar{
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
    return this
}

fun Calendar.toDayEnd(): Calendar{
    this.set(Calendar.HOUR_OF_DAY, 23)
    this.set(Calendar.MINUTE, 59)
    this.set(Calendar.SECOND, 59)
    this.set(Calendar.MILLISECOND, 999)
    return this
}


fun Calendar.timeSet(hour: Int, minute: Int, second: Int = 0): Calendar{
    this.set(Calendar.HOUR_OF_DAY, hour)
    this.set(Calendar.MINUTE, minute)
    this.set(Calendar.SECOND, second)
    return this
}

fun Calendar.timeSet(timestamp: Long = 0): Calendar{
    timeInMillis = timestamp
    return this
}

fun Calendar.isBefore(target: Calendar): Boolean{
    return this.time < target.time
}

/**
 * 格式化秒内容(单位：s)
 * @return this > 60s "xxx分钟xx秒" : "xx秒"
 */
fun Long.durationBalanceFormat(): CharSequence{
    val builder = StringBuilder()
    if(this >= 60L){
        builder.append("${this/60L}分钟")
        if(this%60L > 0){
            builder.append("${this%60L}秒")
        }
    }else{
        builder.append("${this}秒")
    }
    return builder
}

/**
 * 时长格式化(单位：s)
 * @return(H时m分s秒)，时长不足不补位(例：301秒->5分1秒 )
 */
fun Int.secondDurationFormat(): CharSequence{
    val builder = StringBuilder()
    if(this >= 3600){
        builder.append("${this/3600}时")
    }
    if(this >= 60){
        builder.append("${(this%3600)/60}分")
    }
    builder.append("${this%60}秒")
    return builder
}






package com.mellivora.base.expansion

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import java.util.Collections
import java.util.regex.Pattern

/**
 * 获取指定颜色的文本
 * @param color： 0x00000000
 */
fun getColorText(text: CharSequence?, @ColorInt color: Int): CharSequence?{
    if(text.isNullOrEmpty()) return text
    val builder = SpannableStringBuilder(text)
    val oneSpan = ForegroundColorSpan(color)
    builder.setSpan(oneSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return builder
}

/**
 * 获取指定颜色的文本
 * @param color： #000000
 */
fun getColorText(text: CharSequence?, color: String?): CharSequence?{
    if(text.isNullOrEmpty()) return text
    return try {
       val colorInt = Color.parseColor(color)
        getColorText(text, colorInt)
    }catch (e: Throwable){
        text
    }
}

/**
 * 获取带颜色带文字(可忽略异常)
 */
fun getColorText(blockGetText:()-> CharSequence?, blockGetColor:() -> String?): CharSequence?{
    try {
        val text = blockGetText.invoke()
        if(text.isNullOrEmpty()) return text
        return try {
            val colorInt = Color.parseColor(blockGetColor.invoke())
            getColorText(text, colorInt)
        }catch (e: Throwable){
            text
        }
    }catch (e: Throwable){
        e.printStackTrace()
        return null
    }
}

/**
 * 获取指定位置文本
 */
fun CharSequence.subStringAt(index: Int): String{
    return try {
        this[index].toString()
    }catch (e: Throwable){
        e.printStackTrace()
        ""
    }
}

/**
 * 获取指定大小的文本
 * @param pxSize: px值
 */
fun getSizeText(text: CharSequence?, pxSize: Int): CharSequence? {
    if (text.isNullOrEmpty()) return text
    val builder = SpannableStringBuilder(text)
    val sizeSpan = AbsoluteSizeSpan(pxSize, false)
    builder.setSpan(sizeSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return builder
}

/**
 * 获取指定大小的文本
 * @param size: @dimens/sp_15
 */
fun getSizeTextForResource(text: CharSequence?, @DimenRes size: Int): CharSequence? {
    return getSizeText(text, getResDimension(size))
}

/**
 * 获取指定颜色+大小的文本
 * @param color: 0x00000000
 * @param size: @dimens/sp_15
 */
fun getColorAndSizeForResource(text: CharSequence?, @ColorInt color: Int, @DimenRes size: Int): CharSequence? {
    if (text.isNullOrEmpty()) return text
    val builder = SpannableStringBuilder(text)
    val colorSpan = ForegroundColorSpan(color)
    val textSpan = AbsoluteSizeSpan(getResDimension(size), false)
    builder.setSpan(colorSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    builder.setSpan(textSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return builder
}


fun SpannableStringBuilder?.appendColorText(text: CharSequence?, @ColorInt color: Int): SpannableStringBuilder?{
    text?.let {
        this?.append(getColorText(it, color))
    }
    return this
}

fun SpannableStringBuilder?.appendColorText(text: CharSequence?, color: String?): SpannableStringBuilder?{
    text?.let {
        this?.append(getColorText(it, color))
    }
    return this
}


fun SpannableStringBuilder?.appendSizeText(text: CharSequence?, pxSize: Int): SpannableStringBuilder?{
    text?.let {
        this?.append(getSizeText(it, pxSize))
    }
    return this
}

fun SpannableStringBuilder?.appendSizeTextForResource(text: CharSequence?, @DimenRes size: Int): SpannableStringBuilder? {
    text?.let {
        this?.append(getSizeTextForResource(it, size))
    }
    return this
}


fun SpannableStringBuilder?.appendColorAndSizeTextForResource(text: CharSequence?,  @ColorInt color: Int, @DimenRes size: Int): SpannableStringBuilder? {
    text?.let {
        this?.append(getColorAndSizeForResource(it, color, size))
    }
    return this
}


/**
 * 清除小数点后的0
 */
fun CharSequence?.removePointZero(): CharSequence?{
    this ?: return null
    var data = this
    if (data.contains(".")) {
        if (data.endsWith(".")) {
            return data.substring(0, data.length - 1)
        }
        if (data.endsWith("0")) {
            data = data.substring(0, data.length - 1)
            return data.removePointZero()
        }
    }
    return data
}

/**
 * 每个字符中间插入指定文本
 */
fun CharSequence?.joinToString(join: CharSequence = ""): CharSequence?{
    if(this == null) return null
    val builder = StringBuilder()
    this.forEach {
        if(builder.isNotEmpty()){
            builder.append(join)
        }
        builder.append(it)
    }
    return builder
}


/**
 * 格式化文本
 */
fun CharSequence?.formatText(vararg text: String?): String?{
    if (this.isNullOrEmpty()) return this?.toString()
    try {
        return String.format(this.toString(), *text)
    }catch (e: Throwable){
        e.printStackTrace()
    }
    return this.toString()
}


fun CharSequence?.isMobile(): Boolean {
    if (this.isNullOrEmpty()) return false
//    val p = Pattern.compile("^((13[0-9])|(14[5|7])|(15[0-9])|(17[0|7|8])|(18[0-9]))\\d{8}$")
    val p = Pattern.compile("^(1[3-9])\\d{9}$")
    return p.matcher(this).matches()
}


/**
 * (email is null return false)
 * yes return true; no return false;
 * @param email
 * @return boolean
 */
fun CharSequence?.isEmail(): Boolean {
    if (this.isNullOrEmpty()) return false
    val str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
    val p = Pattern.compile(str)
    val m = p.matcher(this)
    return m.matches()
}

/**
 * 每个字符间插入指定的字符串
 */
fun CharSequence?.insertCharGap(text: String): StringBuilder? {
    if (this == null) return null
    val builder = StringBuilder()
    forEach {
        if(builder.isNotEmpty()){
            builder.append(text)
        }
        builder.append(it)
    }
    return builder
}


fun CharSequence?.isIdCard(): Boolean {
    return if (this != null) {
//        val p = Pattern.compile("^((13[0-9])|(14[5|7])|(15[0-9])|(17[0|7|8])|(18[0-9]))\\d{8}$")
        val p = Pattern.compile("^\\d{15}$|^\\d{17}[0-9Xx]$")
        p.matcher(this).matches()
    } else {
        false
    }
}

fun CharSequence.isUrl(): Boolean {
    val regex = "(https?|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    val mat = Pattern.compile(regex).matcher(this.trim())
    return mat.matches()
}



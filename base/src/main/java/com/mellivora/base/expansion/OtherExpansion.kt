@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.mellivora.base.expansion

import android.util.SparseArray
import androidx.core.util.forEach


fun Any?.getLogTag(): String {
    if (this == null) return "null"
//    return this.javaClass.simpleName
    return this.javaClass.name
}
fun Any?.getSimpleLogTag(): String {
    if (this == null) return "null"
    return this.javaClass.simpleName
}


/**
 * 使用String.split("xxx")
 */
@Deprecated("", ReplaceWith("使用String.split(splitted)"))
fun String?.toList(splitted: String = ","): List<String>? {
    return this?.split(splitted)
}

inline fun <T> List<T>?.splitArray(splitSize: Int): List<List<T>>? {
    if(this == null) return null
    val result = arrayListOf<ArrayList<T>>()
    var childArray = arrayListOf<T>()
    this.forEachIndexed { index, t ->
        childArray.add(t)
        if(childArray.size == splitSize){
            result.add(childArray)
            childArray = arrayListOf()
        }
    }
    if(childArray.size > 0){
        result.add(childArray)
    }
    return result
}


inline fun <T> List<T>?.notEmpty(block:(List<T>)->Unit){
    if(this.isNullOrEmpty()){
        return
    }
    block.invoke(this)
}

/**
 * 替换列表里的某条数据
 * @param comparators：数据比较，如果为要替换的数据，返回新的数据
 */
fun <T> MutableList<T>.replaceData(comparators:(T)->T?): Boolean {
   this.forEachIndexed{ i, o ->
       val data = comparators(o)
       if(data != null){
           this.removeAt(i)
           this.add(i, data)
           return true
       }
   }
    return false
}

/**
 * 替换原来的indexValueOf，使用equest比较
 * indexValueOf使用的==比较，而非equest比较
 */
fun <T> SparseArray<T>.mIndexValueOf(it: T): Int{
    forEach { key, value ->
        if(it == value) return key
    }
    return -1
}

inline fun <reified T> tryBlock(block:()->T): T?{
    return try {
        block.invoke()
    }catch (e: Throwable){
        e.printStackTrace()
        null
    }
}


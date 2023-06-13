package com.mellivora.base.expansion

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.Serializable

inline fun <reified T> String?.jsonConvertBean(): T?{
    if(this.isNullOrEmpty()) return null
    return try {
        val type = object : TypeToken<T>() {}.type
        Gson().fromJson(this, type)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}

inline fun <reified T> String?.jsonConvertBeanThrow(typeToken: TypeToken<T> = object : TypeToken<T>(){}): T{
    return Gson().fromJson(this, typeToken.type)
}

fun Any?.toJson(): String?{
    if(this == null){
        return null
    }
    return GsonBuilder().create().toJson(this)
}

inline fun <reified T> List<T>?.toJson(): String?{
    if(this == null){
        return null
    }
    return try {
        GsonBuilder().create().toJson(this)
    }catch (e: Throwable){
        null
    }
}



inline fun <reified T: Serializable> T?.clone(): T?{
    return toJson().jsonConvertBean<T>()
}

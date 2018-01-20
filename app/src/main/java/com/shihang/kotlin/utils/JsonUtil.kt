package com.shihang.kotlin.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


object JsonUtil {


    fun <T> getBean(json: String, t: Class<T>): T? {
        return try {
            Gson().fromJson(json, t)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    fun <T> getList(jsonArray: String, typeToken: TypeToken<MutableList<T>>): MutableList<T>? {
        return try {
            Gson().fromJson<MutableList<T>>(jsonArray, typeToken.type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun toJson(obj: Any): String {
        return Gson().toJson(obj)
    }

    fun <T> getGoodsMap(json: String, t: Class<T>): Map<String, T>? {
        return Gson().fromJson<Map<String, T>>(json, object : TypeToken<HashMap<String, T>>() {}.type)
    }
}

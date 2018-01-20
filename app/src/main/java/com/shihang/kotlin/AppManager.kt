package com.shihang.kotlin

import android.app.Activity
import android.content.Context
import java.util.*


object AppManager {

    private val activitys = ArrayList<Activity>()
    private var resumed: Int = 0

    val foregroundActivity: Activity get() = activitys[activitys.size - 1]

    fun getActivitys(): List<Activity> {
        return activitys
    }

    fun addActivity(activity: Activity) {
        if (!activitys.contains(activity)) {
            activitys.add(activity)
        }
    }

    fun removeActivity(activity: Activity) {
        if (activitys.contains(activity)) {
            activitys.remove(activity)
        }
    }

    fun onActivityResumed(activity: Activity) {
        resumed++
    }

    fun onActivityPaused(activity: Activity) {
        resumed--
    }

    fun appInForeground(): Boolean {
        return resumed > 0
    }

    fun getAppVersionCode(context: Context): Int {
        return context.packageManager.getPackageInfo(context.packageName, 0).versionCode
    }

    fun getAppVersionName(context: Context): String? {
        return context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }


}
package com.mellivora.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.mellivora.base.utils.DeviceIDUtil
import com.mellivora.base.utils.Utils
import java.util.*


object AppManager{

    private val activityManager by lazy { ContextCompat.getSystemService(Utils.getApp(), ActivityManager::class.java) }

    /**
     * 获取当前应用名称
     */
    fun getAppName(context: Context = Utils.getApp()): String? {
        try {
            val packageManager = context.packageManager
            return packageManager.getApplicationLabel(context.applicationInfo).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "UnKnown"
    }

    /**
     * 获取当前本地apk的版本
     * @param mContext
     * @return
     */
    fun getVersionCode(mContext: Context = Utils.getApp()): Int {
        var versionCode = 0
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    /**
     * 获取版本号名称
     * @param context 上下文
     * @return
     */
    fun getVersionName(context: Context = Utils.getApp()): String? {
        var verName: String? = ""
        try {
            verName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }

    /**
     * 获取手机型号
     */
    fun getPhoneModel(): String {
        return "${Build.BRAND}_${Build.MODEL}_系统版本${Build.VERSION.RELEASE}"
    }

    /**
     * 获取设备号
     */
    fun getDeviceId(): String{
        return DeviceIDUtil.getUniqueId(Utils.getApp())
    }

    /**
     * 获取AndroidManifest里的meta-data
     * @param context 上下文
     * @param key     关键值
     */
    fun getMetaData(context: Context = Utils.getApp(), key: String?): String? {
        return try {
            // 获取manifest里的meta-data
            val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val value = appInfo.metaData[key]
            if (value is String) {
                value
            } else {
                (value as? Int)?.toString()
            }
        } catch (e: Exception) {
            null
        }
    }


    private fun getTopActivity(): ComponentName? {
        activityManager?.appTasks?.forEach {
            it.taskInfo?.topActivity?.let { activity ->
                return activity
            }
        }
        return null
    }

    fun getLauncherIntent(): Intent?{
        val intent = getTopActivity()?.let {
            Intent().apply { component = it }
        } ?: run {
            getAppIntent()
        }
        return intent
    }

    fun getAppIntent(): Intent?{
        val context = Utils.getApp()
        return context.packageManager.getLaunchIntentForPackage(context.packageName)
    }









































    /**
     * 注册activity生命周期监听，用于快速查找界面(打印日志)
     */
    fun getUiLifeListener(): Application.ActivityLifecycleCallbacks {
        return object : Application.ActivityLifecycleCallbacks {
            private val TAG = "UiLife(Activity)"
            var uiSize = 0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                uiSize++
                if(activity is FragmentActivity){
                    val fmLifecycle = getFragmentUiLifecycle()
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fmLifecycle, true)
                }
                Log.i(TAG, String.format("%s: onCreated(): taskId: ${activity.taskId}, size:$uiSize", activity.javaClass.name))
            }

            override fun onActivityStarted(activity: Activity) {
                Log.i(TAG, String.format("%s: onStart(): ", activity.javaClass.name))
            }

            override fun onActivityResumed(activity: Activity) {
                Log.i(TAG, String.format("%s: onResumed(): ", activity.javaClass.name))
            }

            override fun onActivityPaused(activity: Activity) {
                Log.i(TAG, String.format("%s: onPause(): ", activity.javaClass.name))
            }

            override fun onActivityStopped(activity: Activity) {
                Log.i(TAG, String.format("%s: onStop(): ", activity.javaClass.name))
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                //Log.i(TAG, String.format("%s: onSaveInstanceState(): ", activity.javaClass.name))
            }

            override fun onActivityDestroyed(activity: Activity) {
                uiSize--
                Log.i(TAG, String.format("%s: onDestory(): taskId: ${activity.taskId}, size:$uiSize", activity.javaClass.name))
            }
        }
    }

    /**
     * 注册Fragment生命周期监听，用于快速查找界面(打印日志)
     */
    private fun getFragmentUiLifecycle(): FragmentManager.FragmentLifecycleCallbacks{
        return object: FragmentManager.FragmentLifecycleCallbacks(){
            val TAG = "UiLife(Fm)"

            override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                //Log.i(TAG, String.format("%s(${f.hashCode()}): onPreAttached(): ", f.javaClass.name))
            }

            override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                //Log.i(TAG, String.format("%s(${f.hashCode()}): onAttached(): ", f.javaClass.name))
            }

            override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                //Log.i(TAG, String.format("%s(${f.hashCode()}): onPreCreated(): ", f.javaClass.name))
            }

            override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onCreated(): ", f.javaClass.name))
            }

            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onViewCreated(): ", f.javaClass.name))
            }

            override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                //Log.i(TAG, String.format("%s(${f.hashCode()}): onActivityCreated(): ", f.javaClass.name))
            }

            override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onStarted(): ", f.javaClass.name))
            }

            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onResumed(): ", f.javaClass.name))
            }

            override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onPaused(): ", f.javaClass.name))
            }

            override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onStopped(): ", f.javaClass.name))
            }

            override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
                //Log.i(TAG, String.format("%s(${f.hashCode()}): onSaveInstanceState(): ", f.javaClass.name))
            }

            override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onViewDestroyed(): ", f.javaClass.name))
            }

            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onDestroyed(): ", f.javaClass.name))
            }

            override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                Log.i(TAG, String.format("%s(${f.hashCode()}): onDetached(): ", f.javaClass.name))
            }
        }
    }

}
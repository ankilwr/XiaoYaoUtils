package com.mellivora.base.launcher

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

open class Launcher<I, R>(caller: ActivityResultCaller, contract: ActivityResultContract<I, R>){

    private var callback: ActivityResultCallback<R>? = null

    private val resultLauncher = caller.registerForActivityResult(contract){
        callback?.onActivityResult(it)
    }

    fun launch(params: I, callback: ActivityResultCallback<R>){
        this.callback = callback
        resultLauncher.launch(params)
    }
}


inline fun <reified T: Launcher<*, *>> LifecycleOwner.launcher(crossinline initializer: () -> T): Lazy<T> {
    val lazy = lazy{ initializer.invoke() }
    lifecycle.addObserver(object: DefaultLifecycleObserver{
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            lazy.value
        }
    })
    return lazy
}


/**
 * 权限启动器
 */
//@see launch(arrayOf(Manifest.permission.RECORD_AUDIO))
class PermissionLauncher(caller: ActivityResultCaller): Launcher<Array<String>, MultiplePermissionsCheck.PermissionResult?>(caller, MultiplePermissionsCheck())

/**
 * 文件管理启动器
 */
//@see launch(arrayOf("image/*", "text/plain"))
class DocumentLauncher(caller: ActivityResultCaller): Launcher<Array<String>, Uri?>(caller, OpenDocument())

/**
 * Intent启动器, 并返回一个ActivityResult
 */
//@see launch(intent)
class IntentLauncher(caller: ActivityResultCaller): Launcher<Intent, ActivityResult>(caller, ActivityResultContracts.StartActivityForResult())

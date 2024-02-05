package com.mellivora.base.launcher

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.app.ActivityCompat
import com.mellivora.base.core.R
import com.mellivora.base.expansion.formatText
import com.mellivora.base.expansion.getResString

class MultiplePermissionsCheck: ActivityResultContract<Array<String>, MultiplePermissionsCheck.PermissionResult?>(){

    override fun createIntent(context: Context, input: Array<String>): Intent {
        return Intent(RequestMultiplePermissions.ACTION_REQUEST_PERMISSIONS)
            .putExtra(RequestMultiplePermissions.EXTRA_PERMISSIONS, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): PermissionResult? {
        if (resultCode != Activity.RESULT_OK) return null
        if (intent == null) return null

        val permissions = intent.getStringArrayExtra(RequestMultiplePermissions.EXTRA_PERMISSIONS)
        val grantResults = intent.getIntArrayExtra(RequestMultiplePermissions.EXTRA_PERMISSION_GRANT_RESULTS)

        if(permissions.isNullOrEmpty()) return null
        if(grantResults == null) return null
        val result = PermissionResult(permissions)
        for (i in permissions.indices) {
            result.resultMap[permissions[i]] = grantResults[i] == PackageManager.PERMISSION_GRANTED
        }
        return result
    }

    class PermissionResult(private val permissions: Array<String>){
        //权限请求结果列表
        val resultMap = hashMapOf<String, Boolean>()

        /**
         * 是否已选择不在提醒
         */
        fun isNeverDenied(context: Activity): Boolean{
            if(isPermissionOpen()) return false
            return !permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(context, it) }
        }

        /**
         * 权限是否都已经授权
         */
        fun isPermissionOpen(): Boolean{
            resultMap.forEach {
                if(!it.value) return false
            }
            return true
        }

        private fun getDeniedText(): String?{
            val permissionText = getDeniedPermissionText().joinToString("、")
            return getResString(R.string.base_permission_denied).formatText(permissionText)
        }

        /**
         * 展示权限拒绝弹窗
         */
        fun showDeniedDialog(context: Activity, tips: String? = getDeniedText()){
            val builder = AlertDialog.Builder(context)
            //builder.setTitle("提示：")
            builder.setMessage(tips)
            builder.setCancelable(true)
            builder.setNegativeButton(android.R.string.cancel) { it, _ ->
                it.dismiss()
            }
            builder.setPositiveButton(android.R.string.ok) { it, _ ->
                openSetting(context)
                it.dismiss()
            }
            builder.create().show()
        }

        /**
         * 打开系统应用-设置
         */
        fun openSetting(context: Context){
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", context.packageName, null)
                context.startActivity(intent)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        
        /**
         * 获取被拒绝权限的名称 
         */
        fun getDeniedPermissionText(): List<String>{
            val list = mutableListOf<String>()
            for (it in permissions){
                //已同意的授权直接跳过
                if(resultMap[it] == true) continue
                //补充被拒绝的权限描述
                when(it){
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        if(!list.contains("存储")) list.add("存储")
                    }
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR -> {
                        if(!list.contains("日历")) list.add("日历")
                    }
                    Manifest.permission.CAMERA -> {
                        if(!list.contains("相机")) list.add("相机")
                    }
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS -> {
                        if(!list.contains("手机账号/通讯录")) list.add("手机账号/通讯录")
                    }
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        if(!list.contains("位置信息")) list.add("位置信息")
                    }
                    Manifest.permission.RECORD_AUDIO -> {
                        if(!list.contains("麦克风")) list.add("麦克风")
                    }
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.ADD_VOICEMAIL,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.ANSWER_PHONE_CALLS -> {
                        if(!list.contains("电话")) list.add("电话")
                    }
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.PROCESS_OUTGOING_CALLS -> {
                        val desc = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) "通话记录" else "电话"
                        if(!list.contains(desc)) list.add(desc)
                    }
                    Manifest.permission.BODY_SENSORS -> {
                        if(!list.contains("身体传感器")) list.add("身体传感器")
                    }
                    Manifest.permission.ACTIVITY_RECOGNITION -> {
                        if(!list.contains("健身运动")) list.add("健身运动")
                    }
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS -> {
                        if(!list.contains("短信")) list.add("短信")
                    }
                }
            }
            return list
        }
    }
}
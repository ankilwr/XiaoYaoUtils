package com.shihang.kotlin.ui


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.view.View
import com.shihang.kotlin.AppManager
import com.shihang.kotlin.dialog.LoadingDialog
import com.shihang.kotlin.extends.getLogTag
import com.shihang.kotlin.extends.showToast
import com.shihang.kotlin.http.HttpManager
import com.shihang.kotlin.permission.PermissionHandler
import com.shihang.kotlin.permission.PermissionUtils
import com.shihang.kotlin.utils.LogUtil
import com.zhy.autolayout.AutoLayoutActivity


open class BaseUI : AutoLayoutActivity() {

    private var loadingDialog: LoadingDialog? = null
    private var mHandler: PermissionHandler? = null

    fun setNightStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    fun setDayStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.addActivity(this)
        LogUtil.i("UiLifeCycle ", "${getLogTag()}: onCreate()")
    }
    override fun onResume() {
        AppManager.onActivityResumed(this)
        LogUtil.i("UiLifeCycle ", "${getLogTag()}: onResume()")
        super.onResume()
    }
    override fun onPause() {
        AppManager.onActivityPaused(this)
        LogUtil.i("UiLifeCycle ", "${getLogTag()}: onPause()")
        super.onPause()
    }
    override fun onDestroy() {
        AppManager.removeActivity(this)
        HttpManager.cancelHttpForTag(this)
        LogUtil.i("UiLifeCycle ", "${getLogTag()}: onDestroy()")
        super.onDestroy()
    }


    fun showLoadingDialog(backCancelEnable: Boolean, needClose: Boolean = true, outCancelEnable: Boolean = false, message: String? = null): LoadingDialog {
        if (loadingDialog == null) loadingDialog = LoadingDialog(this)
        loadingDialog!!.setCanceledOnTouchOutside(outCancelEnable)
        loadingDialog!!.setCancelable(backCancelEnable)
        loadingDialog!!.needCloseEnable = needClose
        loadingDialog!!.setMessage(message)
        if (!loadingDialog!!.isShowing) loadingDialog!!.show()
        return loadingDialog as LoadingDialog
    }
    fun closeLoadingDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.cancel()
        }
    }

    fun getResColor(color: Int): Int {
        return resources.getColor(color)
    }
    fun openUI(ui: Class<*>) {
        val intent = Intent(this, ui)
        startActivity(intent)
    }

    fun showPermissonDialog(message: String) {
        AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage(message)
                .setPositiveButton("去开启") { dialog, which ->
                    //Intent intent = new Intent(ACTION_SETTINGS);
                    val packageUri = Uri.parse("package:" + packageName)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri)
                    startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .show()
    }
    fun requestPermission(permissions: Array<String>, handler: PermissionHandler) {
        if (PermissionUtils.hasSelfPermissions(this, *permissions)) {//判断权限是否已经授权
            handler.onGranted()
        } else {
            mHandler = handler
            ActivityCompat.requestPermissions(this, permissions, 1)//请求权限
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (mHandler == null) return
        if (PermissionUtils.verifyPermissions(*grantResults)) {
            mHandler!!.onGranted()
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this, *permissions)) {
                if (!mHandler!!.onNeverAsk()) {
                    showToast("权限已被拒绝,请在设置-应用-权限中打开")
                }
            } else {
                mHandler!!.onDenied()
            }
        }
    }

}

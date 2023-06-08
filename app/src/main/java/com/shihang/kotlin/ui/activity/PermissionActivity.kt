package com.shihang.kotlin.ui.activity

import android.Manifest
import android.content.Intent
import android.provider.MediaStore
import com.mellivora.base.expansion.hasPermissions
import com.mellivora.base.expansion.setMultipleClick
import com.mellivora.base.launcher.PermissionLauncher
import com.mellivora.base.launcher.launcher
import com.mellivora.base.ui.activity.BaseBindingActivity
import com.shihang.kotlin.databinding.ActivityPermissionBinding

/**
 * 权限请求示例
 */
class PermissionActivity: BaseBindingActivity<ActivityPermissionBinding>(){

    private val permissionLauncher by launcher { PermissionLauncher(this) }


    override fun initBinding(binding: ActivityPermissionBinding) {
        //viewModel.registerLoadingDialog(this, this)
    }

    override fun initViews() {
        viewBinding.appThemeBar.setLeftIconClick{
            finish()
        }
        viewBinding.btnPermission.setMultipleClick {
            //以打开相机为例子
            val permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
            //已经有权限了
            if(hasPermissions(permissions)){
                val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivity(videoIntent)
                return@setMultipleClick
            }
            //去请求权限
            permissionLauncher.launch(permissions){
                //请求结果为空的情况下(直接return即可, 有些人会加loading弹窗的，可在这里关闭)
                if(it == null){
                    return@launch
                }
                //权限全部打开，可进行跳转
                if(it.isPermissionOpen()){
                    val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                    startActivity(videoIntent)
                    return@launch
                }
                //用户拒绝了权限，并且勾选了不再提醒
                if(it.isNeverDenied(this)){
                    //弹窗引导用户到系统-权限设置里面去开启权限
                    it.showDeniedDialog(this)
                }
            }
        }
    }


}

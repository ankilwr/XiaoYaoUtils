package com.shihang.kotlin.manager

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.View
import com.shihang.kotlin.R
import com.shihang.kotlin.dialog.MenuDialog
import com.shihang.kotlin.permission.PermissionHandler
import com.shihang.kotlin.ui.BaseUI
import com.shihang.kotlin.utils.BitmapUtils
import com.shihang.kotlin.utils.FileUtils
import com.shihang.kotlin.utils.UriUtils
import java.io.File


class ImageCutManager(private val activity: BaseUI) {
    private val PICK_REQUEST = 101
    private val CAMERA_REQUEST = 102
    private val CROP_REQUEST = 103
    private var cutResultListener: CutResultListener? = null
    private var cropOutFile: File? = null
    private var cameraOutFile: File? = null
    private val TAG = "ImageCutManager"
    private var dialog: MenuDialog? = null

    interface CutResultListener {
        fun goSystemGallery(): Boolean {
            return true
        }

        fun photoResult(photoFile: String)
    }


    fun setCutResultListener(cutResultListener: CutResultListener) {
        this.cutResultListener = cutResultListener
    }

    fun showImageCutDialog() {
        if (dialog == null) {
            dialog = MenuDialog(activity)
            dialog!!.setMenu1Text("相机").setMenu2Text("相册")
            dialog!!.setMenuClick(View.OnClickListener {
                when (it.id) {
                    R.id.menu1 -> chooseCamear()
                    R.id.menu2 -> if (cutResultListener?.goSystemGallery() == true) chooseImageStore()
                }
            })
        }
        dialog!!.show()
    }

    fun chooseCamear() {
        activity.requestPermission(arrayOf(Manifest.permission.CAMERA), object : PermissionHandler() {
            override fun onGranted() {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val filePath = FileUtils.getSdCacheFilePath(FileUtils.PHOTO, System.currentTimeMillis().toString() + ".jpg")
                cameraOutFile = File(filePath)
                if (Build.VERSION.SDK_INT < 24) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraOutFile))
                } else {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    val uri = FileProvider.getUriForFile(activity, activity.packageName + ".provider", cameraOutFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                }
                activity.startActivityForResult(intent, CAMERA_REQUEST)
            }

            override fun onNeverAsk(): Boolean {
                activity.showPermissonDialog("在设置-应用程序 开启相机权限，以保证功能的正常使用")
                return true
            }
        })
    }

    fun chooseImageStore() {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        activity.startActivityForResult(intent, PICK_REQUEST)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            if (CROP_REQUEST == requestCode) {
                if (cropOutFile != null && cropOutFile!!.exists()) cropOutFile!!.delete()
                if (cameraOutFile != null && cameraOutFile!!.exists()) cameraOutFile!!.delete()
            }
            return
        }
        when (requestCode) {
            CAMERA_REQUEST -> { //拍照完毕
                if (cameraOutFile != null) {
                    val photoPath = cameraOutFile!!.path
                    val degree = BitmapUtils.readPictureDegree(photoPath)
                    val cbitmap = BitmapFactory.decodeFile(photoPath)
                    val newbitmap = BitmapUtils.rotaingImageView(degree, cbitmap)
                    BitmapUtils.bitmapToFile(newbitmap, cameraOutFile!!, 95)
                    cutResultListener?.photoResult(photoPath)
                }
            }
            PICK_REQUEST -> //选择图片完毕
                if (data != null) {
                    cutResultListener?.photoResult(UriUtils.getPath(activity, data.data))
                }
        }
    }

    private fun goCropImage(uri: Uri) {
        cropOutFile = File(FileUtils.getSdCacheFilePath(FileUtils.THUMB, System.currentTimeMillis().toString() + ".jpg"))
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", 100)
        intent.putExtra("outputY", 100)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropOutFile))
        intent.putExtra("return-data", false)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG)
        activity.startActivityForResult(intent, CROP_REQUEST)
    }

}

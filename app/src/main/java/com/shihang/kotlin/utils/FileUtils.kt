package com.shihang.kotlin.utils


import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import com.shihang.kotlin.R
import java.io.File
import java.math.BigDecimal


object FileUtils {

    private var sdCache = "default" //应用的sd卡缓存路径,最好取名为应用名
    var PHOTO = "Photo" //照片存储目录
    var THUMB = "Thumb" //压缩照片存储目录
    var FILES = "files" //接收的文件存储目录



    fun init(context: Context) {
        sdCache = context.resources.getString(R.string.app_name)
    }

    /** 获取应用程序缓存文件夹(Delete)  */
    fun getAppCache(context: Context): File {
        return if (hasSdCard()) context.externalCacheDir else context.cacheDir
    }


    fun createFile(path: String): File {
        val file = File(path)
        if (!file.exists()) file.createNewFile()
        return file
    }

    /** 创建应用缓存文件(Delete)  */
    fun getAppCacheFile(context:Context, fileName: String): File {
        return createFile(getAppCache(context).path + File.separator + fileName)
    }

    /** 获取应用缓存根文件夹(No Delete)  */
    fun getSdCache(): File {
        val path: String
        if (hasSdCard()) {
            path = Environment.getExternalStorageDirectory().toString() + File.separator + sdCache
        } else {
            path = Environment.getDataDirectory().toString() + File.separator + sdCache
        }
        val dir = File(path)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /** 获取应用缓存文件夹(No Delete)  */
    fun getSdCache(folder: String): File {
        val path = getSdCache().path + File.separator + folder
        val dir = File(path)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /** 创建应用缓存文件(No Delete)  */
    fun getSdCacheFile(folderName: String, fileName: String): File {
        val folderPath = getSdCache().path + File.separator + folderName
        val dir = File(folderPath)
        if (!dir.exists()) dir.mkdirs()
        val path = dir.path + File.separator + fileName
        return createFile(path)
    }

    fun getSdCacheFilePath(folderName: String, fileName: String): String {
        val folderPath = getSdCache().path + File.separator + folderName
        val dir = File(folderPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir.path + File.separator + fileName
    }

    fun getFileOrDirSize(file: File?): Long {
        if (file == null) return 0
        if (!file.exists()) return 0
        if (!file.isDirectory) return file.length()

        var length: Long = 0
        val list = file.listFiles()
        if (list != null) { // 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
            for (item in list) {
                length += getFileOrDirSize(item)
            }
        }
        return length
    }

    /**  获取文件大小    */
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (i in fileList.indices) {
                size += if (fileList[i].isDirectory) getFolderSize(fileList[i]) else fileList[i].length()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //return size/1048576; MB
        return size
    }

    /** 删除指定目录下文件及目录  (第二个参数为是否要删除该文件夹，false为清空文件夹但不删除)  */
    fun deleteFolderFile(filePath: String, deleteThisPath: Boolean): Boolean {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                val file = File(filePath)
                if (file.isDirectory) {// 处理目录
                    val files = file.listFiles()
                    for (i in files.indices) {
                        deleteFolderFile(files[i].absolutePath, true)
                    }
                }
                if (deleteThisPath && (!file.isDirectory || file.listFiles().size == 0)) {
                    // 如果是文件，删除 || 文件夹是空的，删除
                    file.delete()
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

        }
        return true
    }


    fun getFile(context: Context, uri: Uri): File? {
        try {
            val filePath = UriUtils.getPath(context, uri)
            return File(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun hasSdCard(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    /** 格式化单位  */
    fun getFormatSize(size: Double): String {
        var size = size
        //		double kiloByte = size/1024;
        //		if(kiloByte < 1) {
        //			return size + "Byte(s)";
        //		}
        //		double megaByte = kiloByte/1024;
        //		if(megaByte < 1) {
        //			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
        //			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        //		}
        //		double gigaByte = megaByte/1024;
        //		if(gigaByte < 1) {
        //			BigDecimal result2  = new BigDecimal(Double.toString(megaByte));
        //			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        //		}
        //		double teraBytes = gigaByte/1024;
        //		if(teraBytes < 1) {
        //			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
        //			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        //		}
        //		BigDecimal result4 = new BigDecimal(teraBytes);
        //		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
        if (size < 1024) {
            return size.toString() + "Byte(s)"
        }
        size /= 1024.0
        if (size < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(size))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }
        size /= 1024.0
        if (size < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(size))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        size /= 1024.0
        if (size < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(size))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(size)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }


    fun getPath(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (cursor != null) cursor.close()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            filePath = uri.path
        }
        return filePath
    }

}

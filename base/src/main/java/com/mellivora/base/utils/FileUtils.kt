package com.mellivora.base.utils


import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.core.content.FileProvider
import com.mellivora.base.AppManager
import com.mellivora.base.expansion.isSpace
import java.io.BufferedInputStream
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.temporal.TemporalQueries.precision
import java.util.Collections
import javax.net.ssl.HttpsURLConnection


object FileUtils {

    object MemoryConstants {
        const val BYTE = 1
        const val KB = 1024
        const val MB = 1048576
        const val GB = 1073741824
    }

    private var sdCache = AppManager.getAppName(Utils.getApp()) //应用的sd卡缓存路径,最好取名为应用名
    var PHOTO = Environment.DIRECTORY_DCIM //照片存储目录
    var THUMB = "Thumb" //压缩照片存储目录
    var Temp = "Temp" //临时文件(用完即删)
    var FILES = "files" //接收的文件存储目录
    var Video = "video" //缓存视屏的路径
    var DownLoads = "DownLoads" //接收的文件存储目录

    private val LINE_SEP = System.getProperty("line.separator")


    /**
     * Return the file by path.
     *
     * @param filePath The path of file.
     * @return the file
     */
    fun getFileByPath(filePath: String?): File? {
        return if (filePath.isSpace()) null else File(filePath)
    }

    /**
     * Return whether the file exists.
     *
     * @param file The file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isFileExists(file: File?): Boolean {
        if (file == null) return false
        return if (file.exists()) {
            true
        } else isFileExists(file.absolutePath)
    }

    /**
     * Return whether the file exists.
     *
     * @param filePath The path of file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isFileExists(filePath: String?): Boolean {
        val file = getFileByPath(filePath) ?: return false
        return if (file.exists()) {
            true
        } else isFileExistsApi29(filePath)
    }

    private fun isFileExistsApi29(filePath: String?): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                val uri = Uri.parse(filePath)
                val cr = Utils.getApp().contentResolver
                val afd = cr.openAssetFileDescriptor(uri, "r") ?: return false
                try {
                    afd.close()
                } catch (ignore: IOException) {
                }
            } catch (e: FileNotFoundException) {
                return false
            }
            return true
        }
        return false
    }

    /**
     * Rename the file.
     *
     * @param filePath The path of file.
     * @param newName  The new name of file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun rename(filePath: String?, newName: String): Boolean {
        return rename(getFileByPath(filePath), newName)
    }

    /**
     * Rename the file.
     *
     * @param file    The file.
     * @param newName The new name of file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun rename(file: File?, newName: String): Boolean {
        // file is null then return false
        if (file == null) return false
        // file doesn't exist then return false
        if (!file.exists()) return false
        // the new name is space then return false
        if (newName.isSpace()) return false
        // the new name equals old name then return true
        if (newName == file.name) return true
        val newFile = File(file.parent + File.separator + newName)
        // the new name of file exists then return false
        return (!newFile.exists()
            && file.renameTo(newFile))
    }

    /**
     * Return whether it is a directory.
     *
     * @param dirPath The path of directory.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isDir(dirPath: String?): Boolean {
        return isDir(getFileByPath(dirPath))
    }

    /**
     * Return whether it is a directory.
     *
     * @param file The file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isDir(file: File?): Boolean {
        return file != null && file.exists() && file.isDirectory
    }

    /**
     * Return whether it is a file.
     *
     * @param filePath The path of file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isFile(filePath: String?): Boolean {
        return isFile(getFileByPath(filePath))
    }

    /**
     * Return whether it is a file.
     *
     * @param file The file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isFile(file: File?): Boolean {
        return file != null && file.exists() && file.isFile
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * @param dirPath The path of directory.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    fun createOrExistsDir(dirPath: String?): Boolean {
        return createOrExistsDir(getFileByPath(dirPath))
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param filePath The path of file.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    fun createOrExistsFile(filePath: String?): Boolean {
        return createOrExistsFile(getFileByPath(filePath))
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists()) return file.isFile
        return if (!createOrExistsDir(file.parentFile)) false else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Create a file if it doesn't exist, otherwise delete old file before creating.
     *
     * @param filePath The path of file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun createFileByDeleteOldFile(filePath: String?): Boolean {
        return createFileByDeleteOldFile(getFileByPath(filePath))
    }

    /**
     * Create a file if it doesn't exist, otherwise delete old file before creating.
     *
     * @param file The file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun createFileByDeleteOldFile(file: File?): Boolean {
        if (file == null) return false
        // file exists and unsuccessfully delete then return false
        if (file.exists() && !file.delete()) return false
        return if (!createOrExistsDir(file.parentFile)) false else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Copy the directory or file.
     *
     * @param srcPath  The path of source.
     * @param destPath The path of destination.
     * @return `true`: success<br></br>`false`: fail
     */
    fun copy(srcPath: String?,
        destPath: String?): Boolean {
        return copy(getFileByPath(srcPath), getFileByPath(destPath), null)
    }

    /**
     * Copy the directory or file.
     *
     * @param srcPath  The path of source.
     * @param destPath The path of destination.
     * @param listener The replace listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun copy(srcPath: String?,
        destPath: String?,
        listener: OnReplaceListener?): Boolean {
        return copy(getFileByPath(srcPath), getFileByPath(destPath), listener)
    }

    /**
     * Copy the directory or file.
     *
     * @param src  The source.
     * @param dest The destination.
     * @return `true`: success<br></br>`false`: fail
     */
    fun copy(src: File?,
        dest: File?): Boolean {
        return copy(src, dest, null)
    }

    /**
     * Copy the directory or file.
     *
     * @param src      The source.
     * @param dest     The destination.
     * @param listener The replace listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun copy(src: File?,
        dest: File?,
        listener: OnReplaceListener?): Boolean {
        if (src == null) return false
        return if (src.isDirectory) {
            copyDir(src, dest, listener)
        } else copyFile(src, dest, listener)
    }

    /**
     * Copy the directory.
     *
     * @param srcDir   The source directory.
     * @param destDir  The destination directory.
     * @param listener The replace listener.
     * @return `true`: success<br></br>`false`: fail
     */
    private fun copyDir(srcDir: File,
        destDir: File?,
        listener: OnReplaceListener?): Boolean {
        return copyOrMoveDir(srcDir, destDir, listener, false)
    }

    /**
     * Copy the file.
     *
     * @param srcFile  The source file.
     * @param destFile The destination file.
     * @param listener The replace listener.
     * @return `true`: success<br></br>`false`: fail
     */
    private fun copyFile(srcFile: File,
        destFile: File?,
        listener: OnReplaceListener?): Boolean {
        return copyOrMoveFile(srcFile, destFile, listener, false)
    }

    /**
     * Move the directory or file.
     *
     * @param srcPath  The path of source.
     * @param destPath The path of destination.
     * @return `true`: success<br></br>`false`: fail
     */
    fun move(srcPath: String?,
        destPath: String?): Boolean {
        return move(getFileByPath(srcPath), getFileByPath(destPath), null)
    }

    /**
     * Move the directory or file.
     *
     * @param srcPath  The path of source.
     * @param destPath The path of destination.
     * @param listener The replace listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun move(srcPath: String?,
        destPath: String?,
        listener: OnReplaceListener?): Boolean {
        return move(getFileByPath(srcPath), getFileByPath(destPath), listener)
    }

    /**
     * Move the directory or file.
     *
     * @param src  The source.
     * @param dest The destination.
     * @return `true`: success<br></br>`false`: fail
     */
    fun move(src: File?,
        dest: File?): Boolean {
        return move(src, dest, null)
    }

    /**
     * Move the directory or file.
     *
     * @param src      The source.
     * @param dest     The destination.
     * @param listener The replace listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun move(src: File?,
        dest: File?,
        listener: OnReplaceListener?): Boolean {
        if (src == null) return false
        return if (src.isDirectory) {
            moveDir(src, dest, listener)
        } else moveFile(src, dest, listener)
    }

    /**
     * Move the directory.
     *
     * @param srcDir   The source directory.
     * @param destDir  The destination directory.
     * @param listener The replace listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun moveDir(srcDir: File?,
        destDir: File?,
        listener: OnReplaceListener?): Boolean {
        return copyOrMoveDir(srcDir, destDir, listener, true)
    }

    /**
     * Move the file.
     *
     * @param srcFile  The source file.
     * @param destFile The destination file.
     * @param listener The replace listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun moveFile(srcFile: File?,
        destFile: File?,
        listener: OnReplaceListener?): Boolean {
        return copyOrMoveFile(srcFile, destFile, listener, true)
    }

    private fun copyOrMoveDir(srcDir: File?,
        destDir: File?,
        listener: OnReplaceListener?,
        isMove: Boolean): Boolean {
        if (srcDir == null || destDir == null) return false
        // destDir's path locate in srcDir's path then return false
        val srcPath = srcDir.path + File.separator
        val destPath = destDir.path + File.separator
        if (destPath.contains(srcPath)) return false
        if (!srcDir.exists() || !srcDir.isDirectory) return false
        if (!createOrExistsDir(destDir)) return false
        val files = srcDir.listFiles()
        if (files != null && files.size > 0) {
            for (file in files) {
                val oneDestFile = File(destPath + file.name)
                if (file.isFile) {
                    if (!copyOrMoveFile(file, oneDestFile, listener, isMove)) return false
                } else if (file.isDirectory) {
                    if (!copyOrMoveDir(file, oneDestFile, listener, isMove)) return false
                }
            }
        }
        return !isMove || deleteDir(srcDir)
    }

    private fun copyOrMoveFile(srcFile: File?,
        destFile: File?,
        listener: OnReplaceListener?,
        isMove: Boolean): Boolean {
        if (srcFile == null || destFile == null) return false
        // srcFile equals destFile then return false
        if (srcFile == destFile) return false
        // srcFile doesn't exist or isn't a file then return false
        if (!srcFile.exists() || !srcFile.isFile) return false
        if (destFile.exists()) {
            if (listener == null || listener.onReplace(
                    srcFile,
                    destFile
                )
            ) { // require delete the old file
                if (!destFile.delete()) { // unsuccessfully delete then return false
                    return false
                }
            } else {
                return true
            }
        }
        return if (!createOrExistsDir(destFile.parentFile)) false else try {
            (FileIOUtils.writeFileFromIS(destFile.absolutePath, FileInputStream(srcFile))
                && !(isMove && !deleteFile(srcFile)))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Delete the directory.
     *
     * @param filePath The path of file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun delete(filePath: String?): Boolean {
        return delete(getFileByPath(filePath))
    }

    /**
     * Delete the directory.
     *
     * @param file The file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun delete(file: File?): Boolean {
        if (file == null) return false
        return if (file.isDirectory) {
            deleteDir(file)
        } else deleteFile(file)
    }

    /**
     * Delete the directory.
     *
     * @param dir The directory.
     * @return `true`: success<br></br>`false`: fail
     */
    private fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.size > 0) {
            for (file in files) {
                if (file.isFile) {
                    if (!file.delete()) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return dir.delete()
    }

    /**
     * Delete the file.
     *
     * @param file The file.
     * @return `true`: success<br></br>`false`: fail
     */
    private fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    /**
     * Delete the all in directory.
     *
     * @param dirPath The path of directory.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteAllInDir(dirPath: String?): Boolean {
        return deleteAllInDir(getFileByPath(dirPath))
    }

    /**
     * Delete the all in directory.
     *
     * @param dir The directory.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteAllInDir(dir: File?): Boolean {
        return deleteFilesInDirWithFilter(dir) { true }
    }

    /**
     * Delete all files in directory.
     *
     * @param dirPath The path of directory.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteFilesInDir(dirPath: String?): Boolean {
        return deleteFilesInDir(getFileByPath(dirPath))
    }

    /**
     * Delete all files in directory.
     *
     * @param dir The directory.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteFilesInDir(dir: File?): Boolean {
        return deleteFilesInDirWithFilter(dir) { pathname -> pathname.isFile }
    }

    /**
     * Delete all files that satisfy the filter in directory.
     *
     * @param dirPath The path of directory.
     * @param filter  The filter.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteFilesInDirWithFilter(dirPath: String?,
        filter: FileFilter?): Boolean {
        return deleteFilesInDirWithFilter(getFileByPath(dirPath), filter)
    }

    /**
     * Delete all files that satisfy the filter in directory.
     *
     * @param dir    The directory.
     * @param filter The filter.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteFilesInDirWithFilter(dir: File?, filter: FileFilter?): Boolean {
        if (dir == null || filter == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (filter.accept(file)) {
                    if (file.isFile) {
                        if (!file.delete()) return false
                    } else if (file.isDirectory) {
                        if (!deleteDir(file)) return false
                    }
                }
            }
        }
        return true
    }

    /**
     * Return the files in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dirPath The path of directory.
     * @return the files in directory
     */
    fun listFilesInDir(dirPath: String?): List<File?>? {
        return listFilesInDir(dirPath, null)
    }

    /**
     * Return the files in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dir The directory.
     * @return the files in directory
     */
    fun listFilesInDir(dir: File?): List<File?>? {
        return listFilesInDir(dir, null)
    }

    /**
     * Return the files in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dirPath    The path of directory.
     * @param comparator The comparator to determine the order of the list.
     * @return the files in directory
     */
    fun listFilesInDir(dirPath: String?, comparator: Comparator<File?>?): List<File?>? {
        return listFilesInDir(getFileByPath(dirPath), false, comparator)
    }

    /**
     * Return the files in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dir        The directory.
     * @param comparator The comparator to determine the order of the list.
     * @return the files in directory
     */
    fun listFilesInDir(dir: File?, comparator: Comparator<File?>?): List<File?>? {
        return listFilesInDir(dir, false, comparator)
    }

    /**
     * Return the files in directory.
     *
     * @param dirPath     The path of directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files in directory
     */
    fun listFilesInDir(dirPath: String?, isRecursive: Boolean): List<File?>? {
        return listFilesInDir(getFileByPath(dirPath), isRecursive)
    }

    /**
     * Return the files in directory.
     *
     * @param dir         The directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files in directory
     */
    fun listFilesInDir(dir: File?, isRecursive: Boolean): List<File?>? {
        return listFilesInDir(dir, isRecursive, null)
    }

    /**
     * Return the files in directory.
     *
     * @param dirPath     The path of directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @param comparator  The comparator to determine the order of the list.
     * @return the files in directory
     */
    fun listFilesInDir(dirPath: String?,
        isRecursive: Boolean,
        comparator: Comparator<File?>?): List<File?>? {
        return listFilesInDir(getFileByPath(dirPath), isRecursive, comparator)
    }

    /**
     * Return the files in directory.
     *
     * @param dir         The directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @param comparator  The comparator to determine the order of the list.
     * @return the files in directory
     */
    fun listFilesInDir(dir: File?,
        isRecursive: Boolean,
        comparator: Comparator<File?>?): List<File?>? {
        return listFilesInDirWithFilter(dir, { true }, isRecursive, comparator)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dirPath The path of directory.
     * @param filter  The filter.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dirPath: String?,
        filter: FileFilter): List<File?>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dir    The directory.
     * @param filter The filter.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dir: File?,
        filter: FileFilter): List<File?>? {
        return listFilesInDirWithFilter(dir, filter, false, null)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dirPath    The path of directory.
     * @param filter     The filter.
     * @param comparator The comparator to determine the order of the list.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dirPath: String?,
        filter: FileFilter,
        comparator: Comparator<File?>?): List<File?>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, comparator)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dir        The directory.
     * @param filter     The filter.
     * @param comparator The comparator to determine the order of the list.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dir: File?,
        filter: FileFilter,
        comparator: Comparator<File?>?): List<File?>? {
        return listFilesInDirWithFilter(dir, filter, false, comparator)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dirPath     The path of directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dirPath: String?,
        filter: FileFilter,
        isRecursive: Boolean): List<File?>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dir         The directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dir: File?,
        filter: FileFilter,
        isRecursive: Boolean): List<File?>? {
        return listFilesInDirWithFilter(dir, filter, isRecursive, null)
    }


    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dirPath     The path of directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @param comparator  The comparator to determine the order of the list.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dirPath: String?,
        filter: FileFilter,
        isRecursive: Boolean,
        comparator: Comparator<File?>?): List<File?>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive, comparator)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dir         The directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @param comparator  The comparator to determine the order of the list.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dir: File?,
        filter: FileFilter,
        isRecursive: Boolean,
        comparator: Comparator<File?>?): List<File?>? {
        val files = listFilesInDirWithFilterInner(dir, filter, isRecursive)
        if (comparator != null) {
            Collections.sort(files, comparator)
        }
        return files
    }

    private fun listFilesInDirWithFilterInner(dir: File?,
        filter: FileFilter,
        isRecursive: Boolean): List<File?> {
        val list: MutableList<File?> = ArrayList()
        if (!isDir(dir)) return list
        val files = dir!!.listFiles()
        if (files != null && files.size > 0) {
            for (file in files) {
                if (filter.accept(file)) {
                    list.add(file)
                }
                if (isRecursive && file.isDirectory) {
                    list.addAll(listFilesInDirWithFilterInner(file, filter, true))
                }
            }
        }
        return list
    }

    /**
     * Return the time that the file was last modified.
     *
     * @param filePath The path of file.
     * @return the time that the file was last modified
     */
    fun getFileLastModified(filePath: String?): Long {
        return getFileLastModified(getFileByPath(filePath))
    }

    /**
     * Return the time that the file was last modified.
     *
     * @param file The file.
     * @return the time that the file was last modified
     */
    fun getFileLastModified(file: File?): Long {
        return file?.lastModified() ?: -1
    }

    /**
     * Return the charset of file simply.
     *
     * @param filePath The path of file.
     * @return the charset of file simply
     */
    fun getFileCharsetSimple(filePath: String?): String? {
        return getFileCharsetSimple(getFileByPath(filePath))
    }

    /**
     * Return the charset of file simply.
     *
     * @param file The file.
     * @return the charset of file simply
     */
    fun getFileCharsetSimple(file: File?): String? {
        if (file == null) return ""
        if (isUtf8(file)) return "UTF-8"
        var p = 0
        var `is`: InputStream? = null
        try {
            `is` = BufferedInputStream(FileInputStream(file))
            p = (`is`.read() shl 8) + `is`.read()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return when (p) {
            0xfffe -> "Unicode"
            0xfeff -> "UTF-16BE"
            else -> "GBK"
        }
    }

    /**
     * Return whether the charset of file is utf8.
     *
     * @param filePath The path of file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isUtf8(filePath: String?): Boolean {
        return isUtf8(getFileByPath(filePath))
    }

    /**
     * Return whether the charset of file is utf8.
     *
     * @param file The file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isUtf8(file: File?): Boolean {
        if (file == null) return false
        var `is`: InputStream? = null
        try {
            val bytes = ByteArray(24)
            `is` = BufferedInputStream(FileInputStream(file))
            val read = `is`.read(bytes)
            return if (read != -1) {
                val readArr = ByteArray(read)
                System.arraycopy(bytes, 0, readArr, 0, read)
                isUtf8(readArr) == 100
            } else {
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return false
    }

    /**
     * UTF-8编码方式
     * ----------------------------------------------
     * 0xxxxxxx
     * 110xxxxx 10xxxxxx
     * 1110xxxx 10xxxxxx 10xxxxxx
     * 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
     */
    private fun isUtf8(raw: ByteArray): Int {
        var i: Int
        val len: Int
        var utf8 = 0
        var ascii = 0
        if (raw.size > 3) {
            if (raw[0] == 0xEF.toByte() && raw[1] == 0xBB.toByte() && raw[2] == 0xBF.toByte()) {
                return 100
            }
        }
        len = raw.size
        var child = 0
        i = 0
        while (i < len) {

            // UTF-8 byte shouldn't be FF and FE
            if (raw[i].toInt() and 0xFF.toByte().toInt() == 0xFF.toByte()
                    .toInt() || raw[i].toInt() and 0xFE.toByte().toInt() == 0xFE.toByte().toInt()
            ) {
                return 0
            }
            if (child == 0) {
                // ASCII format is 0x0*******
                if (raw[i].toInt() and 0x7F.toByte()
                        .toInt() == raw[i].toInt() && raw[i].toInt() != 0
                ) {
                    ascii++
                } else if (raw[i].toInt() and 0xC0.toByte().toInt() == 0xC0.toByte().toInt()) {
                    // 0x11****** maybe is UTF-8
                    for (bit in 0..7) {
                        child = if ((0x80 shr bit).toByte()
                                .toInt() and raw[i].toInt() == (0x80 shr bit).toByte()
                                .toInt()
                        ) {
                            bit
                        } else {
                            break
                        }
                    }
                    utf8++
                }
                i++
            } else {
                child = if (raw.size - i > child) child else raw.size - i
                var currentNotUtf8 = false
                for (children in 0 until child) {
                    // format must is 0x10******
                    if (raw[i + children].toInt() and 0x80.toByte().toInt() != 0x80.toByte()
                            .toInt()
                    ) {
                        if (raw[i + children].toInt() and 0x7F.toByte()
                                .toInt() == raw[i + children].toInt() && raw[i].toInt() != 0
                        ) {
                            // ASCII format is 0x0*******
                            ascii++
                        }
                        currentNotUtf8 = true
                    }
                }
                if (currentNotUtf8) {
                    utf8--
                    i++
                } else {
                    utf8 += child
                    i += child
                }
                child = 0
            }
        }
        // UTF-8 contains ASCII
        return if (ascii == len) {
            100
        } else (100 * ((utf8 + ascii).toFloat() / len.toFloat())).toInt()
    }

    /**
     * Return the number of lines of file.
     *
     * @param filePath The path of file.
     * @return the number of lines of file
     */
    fun getFileLines(filePath: String?): Int {
        return getFileLines(getFileByPath(filePath))
    }

    /**
     * Return the number of lines of file.
     *
     * @param file The file.
     * @return the number of lines of file
     */
    fun getFileLines(file: File?): Int {
        var count = 1
        var `is`: InputStream? = null
        try {
            `is` = BufferedInputStream(FileInputStream(file))
            val buffer = ByteArray(1024)
            var readChars: Int
            if (LINE_SEP.endsWith("\n")) {
                while (`is`.read(buffer, 0, 1024).also { readChars = it } != -1) {
                    for (i in 0 until readChars) {
                        if (buffer[i] == '\n'.code.toByte()) ++count
                    }
                }
            } else {
                while (`is`.read(buffer, 0, 1024).also { readChars = it } != -1) {
                    for (i in 0 until readChars) {
                        if (buffer[i] == '\r'.code.toByte()) ++count
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return count
    }

    /**
     * Return the size.
     *
     * @param filePath The path of file.
     * @return the size
     */
    fun getSize(filePath: String?): String? {
        return getSize(getFileByPath(filePath))
    }

    /**
     * Return the size.
     *
     * @param file The directory.
     * @return the size
     */
    fun getSize(file: File?): String? {
        if (file == null) return ""
        return if (file.isDirectory) {
            getDirSize(file)
        } else getFileSize(file)
    }

    /**
     * Return the size of directory.
     *
     * @param dir The directory.
     * @return the size of directory
     */
    private fun getDirSize(dir: File): String? {
        val len = getDirLength(dir)
        return if (len == -1L) "" else byte2FitMemorySize(len)
    }

    /**
     * Return the size of file.
     *
     * @param file The file.
     * @return the length of file
     */
    private fun getFileSize(file: File): String? {
        val len = getFileLength(file)
        return if (len == -1L) "" else byte2FitMemorySize(len)
    }

    /**
     * Return the length.
     *
     * @param filePath The path of file.
     * @return the length
     */
    fun getLength(filePath: String?): Long {
        return getLength(getFileByPath(filePath))
    }

    private fun byte2FitMemorySize(byteSize: Long, precision: Int = 3): String{
        return if (byteSize < 0) {
            throw IllegalArgumentException("byteSize shouldn't be less than zero!")
        } else if (byteSize < MemoryConstants.KB) {
            String.format("%.$precision" + "fB", byteSize.toDouble())
        } else if (byteSize < MemoryConstants.MB) {
            String.format("%.$precision" + "fKB", byteSize.toDouble() / MemoryConstants.KB)
        } else if (byteSize < MemoryConstants.GB) {
            String.format("%.$precision" + "fMB", byteSize.toDouble() / MemoryConstants.MB)
        } else {
            String.format("%.$precision" + "fGB", byteSize.toDouble() / MemoryConstants.GB)
        }
    }

    /**
     * Return the length.
     *
     * @param file The file.
     * @return the length
     */
    fun getLength(file: File?): Long {
        if (file == null) return 0
        return if (file.isDirectory) {
            getDirLength(file)
        } else getFileLength(file)
    }

    /**
     * Return the length of directory.
     *
     * @param dir The directory.
     * @return the length of directory
     */
    private fun getDirLength(dir: File): Long {
        if (!isDir(dir)) return 0
        var len: Long = 0
        val files = dir.listFiles()
        if (files != null && files.size > 0) {
            for (file in files) {
                len += if (file.isDirectory) {
                    getDirLength(file)
                } else {
                    file.length()
                }
            }
        }
        return len
    }

    /**
     * Return the length of file.
     *
     * @param filePath The path of file.
     * @return the length of file
     */
    fun getFileLength(filePath: String): Long {
        val isURL = filePath.matches("[a-zA-z]+://[^\\s]*".toRegex())
        if (isURL) {
            try {
                val conn = URL(filePath).openConnection() as HttpsURLConnection
                conn.setRequestProperty("Accept-Encoding", "identity")
                conn.connect()
                return if (conn.responseCode == 200) {
                    conn.contentLength.toLong()
                } else -1
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return getFileLength(getFileByPath(filePath))
    }

    /**
     * Return the length of file.
     *
     * @param file The file.
     * @return the length of file
     */
    private fun getFileLength(file: File?): Long {
        return if (!isFile(file)) -1 else file!!.length()
    }

    /**
     * Return the MD5 of file.
     *
     * @param filePath The path of file.
     * @return the md5 of file
     */
    fun getFileMD5ToString(filePath: String?): String? {
        val file = if (filePath.isSpace()) null else File(filePath)
        return getFileMD5ToString(file)
    }

    /**
     * Return the MD5 of file.
     *
     * @param file The file.
     * @return the md5 of file
     */
    fun getFileMD5ToString(file: File?): String? {
        return ConvertUtils.bytes2HexString(getFileMD5(file))
    }

    /**
     * Return the MD5 of file.
     *
     * @param filePath The path of file.
     * @return the md5 of file
     */
    fun getFileMD5(filePath: String?): ByteArray? {
        return getFileMD5(getFileByPath(filePath))
    }

    /**
     * Return the MD5 of file.
     *
     * @param file The file.
     * @return the md5 of file
     */
    fun getFileMD5(file: File?): ByteArray? {
        if (file == null) return null
        var dis: DigestInputStream? = null
        try {
            val fis = FileInputStream(file)
            var md = MessageDigest.getInstance("MD5")
            dis = DigestInputStream(fis, md)
            val buffer = ByteArray(1024 * 256)
            while (true) {
                if (dis.read(buffer) <= 0) break
            }
            md = dis.messageDigest
            return md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                dis?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * Return the file's path of directory.
     *
     * @param file The file.
     * @return the file's path of directory
     */
    fun getDirName(file: File?): String? {
        return if (file == null) "" else getDirName(file.absolutePath)
    }

    /**
     * Return the file's path of directory.
     *
     * @param filePath The path of file.
     * @return the file's path of directory
     */
    fun getDirName(filePath: String): String? {
        if (filePath.isSpace()) return ""
        val lastSep = filePath.lastIndexOf(File.separator)
        return if (lastSep == -1) "" else filePath.substring(0, lastSep + 1)
    }

    /**
     * Return the name of file.
     *
     * @param file The file.
     * @return the name of file
     */
    fun getFileName(file: File?): String? {
        return if (file == null) "" else getFileName(file.absolutePath)
    }

    /**
     * Return the name of file.
     *
     * @param filePath The path of file.
     * @return the name of file
     */
    fun getFileName(filePath: String): String? {
        if (filePath.isSpace()) return ""
        val lastSep = filePath.lastIndexOf(File.separator)
        return if (lastSep == -1) filePath else filePath.substring(lastSep + 1)
    }

    /**
     * Return the name of file without extension.
     *
     * @param file The file.
     * @return the name of file without extension
     */
    fun getFileNameNoExtension(file: File?): String? {
        return if (file == null) "" else getFileNameNoExtension(file.path)
    }

    /**
     * Return the name of file without extension.
     *
     * @param filePath The path of file.
     * @return the name of file without extension
     */
    fun getFileNameNoExtension(filePath: String): String? {
        if (filePath.isSpace()) return ""
        val lastPoi = filePath.lastIndexOf('.')
        val lastSep = filePath.lastIndexOf(File.separator)
        if (lastSep == -1) {
            return if (lastPoi == -1) filePath else filePath.substring(0, lastPoi)
        }
        return if (lastPoi == -1 || lastSep > lastPoi) {
            filePath.substring(lastSep + 1)
        } else filePath.substring(lastSep + 1, lastPoi)
    }

    /**
     * Return the extension of file.
     *
     * @param file The file.
     * @return the extension of file
     */
    fun getFileExtension(file: File?): String? {
        return if (file == null) "" else getFileExtension(file.path)
    }

    /**
     * Return the extension of file.
     *
     * @param filePath The path of file.
     * @return the extension of file
     */
    fun getFileExtension(filePath: String): String? {
        if (filePath.isSpace()) return ""
        val lastPoi = filePath.lastIndexOf('.')
        val lastSep = filePath.lastIndexOf(File.separator)
        return if (lastPoi == -1 || lastSep >= lastPoi) "" else filePath.substring(lastPoi + 1)
    }

    /**
     * Notify system to scan the file.
     *
     * @param filePath The path of file.
     */
    fun notifySystemToScan(filePath: String?) {
        notifySystemToScan(getFileByPath(filePath))
    }

    /**
     * Notify system to scan the file.
     *
     * @param file The file.
     */
    fun notifySystemToScan(file: File?) {
        if (file == null || !file.exists()) return
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.parse("file://" + file.absolutePath)
        Utils.getApp().sendBroadcast(intent)
    }

    /**
     * Return the total size of file system.
     *
     * @param anyPathInFs Any path in file system.
     * @return the total size of file system
     */
    fun getFsTotalSize(anyPathInFs: String?): Long {
        if (TextUtils.isEmpty(anyPathInFs)) return 0
        val statFs = StatFs(anyPathInFs)
        val blockSize: Long
        val totalSize: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.blockSizeLong
            totalSize = statFs.blockCountLong
        } else {
            blockSize = statFs.blockSize.toLong()
            totalSize = statFs.blockCount.toLong()
        }
        return blockSize * totalSize
    }

    /**
     * Return the available size of file system.
     *
     * @param anyPathInFs Any path in file system.
     * @return the available size of file system
     */
    fun getFsAvailableSize(anyPathInFs: String?): Long {
        if (TextUtils.isEmpty(anyPathInFs)) return 0
        val statFs = StatFs(anyPathInFs)
        val blockSize: Long
        val availableSize: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.blockSizeLong
            availableSize = statFs.availableBlocksLong
        } else {
            blockSize = statFs.blockSize.toLong()
            availableSize = statFs.availableBlocks.toLong()
        }
        return blockSize * availableSize
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////
    interface OnReplaceListener {
        fun onReplace(srcFile: File?, destFile: File?): Boolean
    }

    fun getPicturesUriJava(): Uri?{
        return getPicturesUri()
    }

    fun getPicturesUri(fileName: String = "IMG_${System.currentTimeMillis()}.jpg", mine: String = "image/jpg"): Uri?{
        val context = Utils.getApp().applicationContext
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            //RELATIVE_PATH 在androidQ以上才生效
            //values.put(MediaStore.Images.Media.RELATIVE_PATH, String.format("Pictures/%s/", context.getString(R.string.app_name)))
            values.put(MediaStore.Images.Media.MIME_TYPE, mine)
            values.put(MediaStore.Images.Media.TITLE, "Image")
            values.put(MediaStore.Images.Media.DESCRIPTION, "this is an image")
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            val fileDir = File(getSdCachePath("Pictures"))
            if (!fileDir.exists()) {
                fileDir.mkdir()
            }
            val mFilePath = "${fileDir.path}/${fileName}"
            FileProvider.getUriForFile(context, "${context.packageName}.provider", File(mFilePath))
        }
    }


    fun hasSdCard(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    //获取SD缓存路径(获取Android10以下的api, 10以上不推荐使用)
    private fun getSdCache(): File {
        val path = if (hasSdCard()) {
            Environment.getExternalStorageDirectory().toString() + File.separator + sdCache
        } else {
            Environment.getDataDirectory().toString() + File.separator + sdCache
        }
        val dir = File(path)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /** 获取应用缓存文件夹(No Delete)  */
    private fun getSdCache(folder: String): File {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Utils.getApp().getExternalFilesDir(folder)!!
        } else {
            val path = getSdCache().path + File.separator + folder
            val dir = File(path)
            if (!dir.exists()) dir.mkdirs()
            dir
        }
    }

    /** 获取SD卡缓存路径(No Delete)  */
    private fun getSdCachePath(folderName: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Utils.getApp().getExternalFilesDir(folderName)!!.path
        } else {
            getSdCache().path + File.separator + folderName
        }
    }

    /**
     * 返回的File可能会不存在
     */
    fun getSdCacheFile(folderName: String, fileName: String): File {
        val path = getSdCache(folderName).path + File.separator + fileName
        return File(path)
    }

    private fun getSdCacheFilePath(folderName: String, fileName: String): String {
        return getSdCache(folderName).path + File.separator + fileName
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
                cursor?.close()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            filePath = uri.path
        }
        return filePath
    }

    fun getUri(context: Context, path: String): Uri{
        return if (Build.VERSION.SDK_INT < 24) {
            Uri.fromFile(File(path))
        } else {
            FileProvider.getUriForFile(context, "${context.packageName}.provider", File(path))
        }
    }


    fun getMimeType(fileName: String): String {
        val fileNameMap = URLConnection.getFileNameMap()
        return fileNameMap.getContentTypeFor(fileName)
    }

    fun isVideoFile(fileName: String): Boolean {
        val mimeType = getMimeType(fileName)
        return !TextUtils.isEmpty(fileName) && mimeType.contains("video/")
    }

    fun isImageFile(fileName: String): Boolean {
        val mimeType = getMimeType(fileName)
        return !TextUtils.isEmpty(fileName) && mimeType.contains("image/")
    }

    fun isAudioFile(fileName: String): Boolean {
        val mimeType = getMimeType(fileName)
        return !TextUtils.isEmpty(fileName) && mimeType.contains("audio/")
    }



    fun copyAssetsToSd(context: Context, assetsName: String, sdPath: String){
        val inputStream = context.assets.open(assetsName)
        val outFile = File(sdPath)
        val fos = FileOutputStream(outFile)
        val buffer = ByteArray(1024)
        var byteCount: Int = inputStream.read(buffer)
        while (byteCount != -1) {
            fos.write(buffer, 0, byteCount)
            byteCount = inputStream.read(buffer)
        }
        fos.flush()
        inputStream.close()
        fos.close()
    }

    fun copy(ips: InputStream, ops: OutputStream){
        val buffer = ByteArray(1024)
        var byteCount: Int = ips.read(buffer)
        while (byteCount != -1) {
            ops.write(buffer, 0, byteCount)
            byteCount = ips.read(buffer)
        }
        ops.flush()
        ips.close()
        ops.close()
    }

    fun getResUri(context: Context, @DrawableRes resId: Int): Uri{
        val resources = context.resources
        return Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${resources.getResourcePackageName(resId)}/${resources.getResourceTypeName(resId)}/${resources.getResourceEntryName(resId)}")
    }


}

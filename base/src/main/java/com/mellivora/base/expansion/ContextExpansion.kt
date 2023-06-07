@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.mellivora.base.expansion

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.mellivora.base.utils.Utils

fun Context?.getActivity(): Activity? {
    if(this == null) return null
    var context = this
    if(context is Activity){
        return context
    }
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun Context.createIntent(cls: Class<*>): Intent{
    val intent = Intent(this, cls)
    if(getActivity() == null){
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return intent
}

/**
 * 所需权限是否已经授权
 */
fun Context.hasPermissions(permissions: Array<String>): Boolean{
    permissions.forEach {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(this.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
    }
    return true
}

inline fun Context.callPhone(phone: String?) {
    val intent = Intent(Intent.ACTION_DIAL)
    val data = Uri.parse("tel:$phone")
    intent.data = data
    startActivity(intent)
}


fun Context?.showToast(text: CharSequence?) {
    if (this == null || text.isNullOrEmpty()) return
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context?.showToast(@StringRes strId: Int) {
    if (this == null) return
    Toast.makeText(this, strId, Toast.LENGTH_SHORT).show()
}

fun showToast(@StringRes strId: Int){
    Utils.getApp().showToast(strId)
}
fun showToast(str: CharSequence?){
    str ?: return
    Utils.getApp().showToast(str)
}

fun getResString(@StringRes strId: Int): String{
    return Utils.getApp().resources.getString(strId)
}

fun getStringArray(arrayId: Int): Array<String>{
    return Utils.getApp().resources.getStringArray(arrayId)
}

fun getResDimension(@DimenRes dimenRes: Int): Int{
    return Utils.getApp().resources.getDimension(dimenRes).toInt()
}

fun getResDrawable(@DrawableRes drawableId: Int): Drawable?{
    return tryBlock {
        ContextCompat.getDrawable(Utils.getApp(), drawableId)
    }
}

fun getResColorStateList(colorId: Int): ColorStateList?{
    return ContextCompat.getColorStateList(Utils.getApp(), colorId)
}


fun getResColor(colorResId: Int): Int{
    return ContextCompat.getColor(Utils.getApp(), colorResId)
}


fun getStateDrawable(attrState: Int, normal: Drawable?, selected: Drawable?): StateListDrawable{
    val stateDrawable = StateListDrawable()
    stateDrawable.addState(intArrayOf(-attrState), normal)
    stateDrawable.addState(intArrayOf(attrState), selected)
    return stateDrawable
}

fun getStateColor(attrState: Int, @ColorInt normal: Int, @ColorInt selected: Int): ColorStateList{
    val attr = arrayOf(
        intArrayOf(-attrState),
        intArrayOf(attrState)
    )
    return ColorStateList(attr, intArrayOf(normal, selected))
}

fun Context.getSelectDrawable(@DrawableRes normal: Int, @DrawableRes selected: Int): StateListDrawable {
    val normalDrawable = ContextCompat.getDrawable(this, normal)
    val selectedDrawable = ContextCompat.getDrawable(this, selected)
    return getStateDrawable(android.R.attr.state_selected, normalDrawable, selectedDrawable)
}

fun getSelectDrawable(@DrawableRes normal: Int, @DrawableRes selected: Int): StateListDrawable {
    return Utils.getApp().getSelectDrawable(normal, selected)
}



fun Context.getStatusBarHeight(): Int {
    try {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return this.resources.getDimensionPixelSize(resourceId)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

fun Activity.setFullEnable(enable: Boolean){
    //WindowCompat.setDecorFitsSystemWindows(window, !enable)
    val flags = window.decorView.systemUiVisibility
    if(enable){
        window.decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }else{
        window.decorView.systemUiVisibility = flags xor View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

fun Activity.setLightModel(enable: Boolean){
    val wic = WindowCompat.getInsetsController(window, window.decorView)
    wic.isAppearanceLightStatusBars = enable
}


fun Context.copyText(text: String?, toast: Boolean = true) {
    val clipboard = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    val clip = ClipData.newPlainText("shunli", text ?: "")
    clipboard.setPrimaryClip(clip)
    if (toast) showToast("已复制到剪贴板")
}


fun Context.getVersionName(): String{
    try {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, 0)
        return info.versionName
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun Context.getSelectDrawable(context: Context, @DrawableRes normal: Int, @DrawableRes selected: Int): StateListDrawable {
    val stateDrawable = StateListDrawable()
    val normalDrawable = ContextCompat.getDrawable(context, normal)
    val selectedDrawable = ContextCompat.getDrawable(context, selected)
    stateDrawable.addState(intArrayOf(-android.R.attr.state_selected), normalDrawable)
    stateDrawable.addState(intArrayOf(android.R.attr.state_selected), selectedDrawable)
    return stateDrawable
}
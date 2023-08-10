package com.mellivora.base.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.mellivora.base.R
import com.mellivora.base.databinding.BaseLayoutThemeTitlebarBinding
import com.mellivora.base.expansion.activity
import com.mellivora.base.expansion.getStatusBarHeight
import com.mellivora.base.expansion.layoutInflater
import com.mellivora.base.expansion.setFullEnable
import com.mellivora.base.expansion.setLightModel
import com.mellivora.base.expansion.setMultipleClick

class AppThemeBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    val fullBinding = BaseLayoutThemeTitlebarBinding.inflate(layoutInflater, this, true)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AppThemeBar)
        if(a.hasValue(R.styleable.AppThemeBar_leftIcon)){
            setLeftIcon(a.getDrawable(R.styleable.AppThemeBar_leftIcon))
        }
        if(a.hasValue(R.styleable.AppThemeBar_leftIconTint)){
            setLeftIconTint(a.getColor(R.styleable.AppThemeBar_leftIconTint, Color.BLACK))
        }
        if(a.hasValue(R.styleable.AppThemeBar_rightIcon)){
            setRightIcon(a.getDrawable(R.styleable.AppThemeBar_rightIcon))
        }
        if(a.hasValue(R.styleable.AppThemeBar_rightIconTint)){
            setRightIconTint(a.getColor(R.styleable.AppThemeBar_rightIconTint, Color.BLACK))
        }
        setBarTitle(a.getString(R.styleable.AppThemeBar_barTitle))
        if(a.hasValue(R.styleable.AppThemeBar_barTitleColor)){
            setTitleTextColor(a.getColor(R.styleable.AppThemeBar_barTitleColor, Color.BLACK))
        }
        setRightMenu(a.getString(R.styleable.AppThemeBar_rightMenu))
        if(a.hasValue(R.styleable.AppThemeBar_rightMenuTint)){
            setRightMenuColor(a.getColor(R.styleable.AppThemeBar_rightMenuTint, Color.BLACK))
        }
        setActionbarLineEnable(a.getBoolean(R.styleable.AppThemeBar_actionbarLineEnable, false))
        if(a.hasValue(R.styleable.AppThemeBar_actionbarLineColor)){
            setActionbarLineColor(a.getColor(R.styleable.AppThemeBar_actionbarLineColor, Color.LTGRAY))
        }
        //是否需要填充状态栏高度
        setFullStatusBarEnable(a.getBoolean(R.styleable.AppThemeBar_fullStatusBar, true))
        //是否需要亮色的状态栏风格
        setLightStatusModel(a.getBoolean(R.styleable.AppThemeBar_lightStatusBarModel, false))
        a.recycle()
    }

    fun setLeftIconClick(click: OnClickListener?){
        fullBinding.back.setMultipleClick {
            click?.onClick(it)
        }
    }

    fun setLeftIcon(@DrawableRes res: Int){
        setLeftIcon(ContextCompat.getDrawable(context, res))
    }

    fun setLeftIcon(res: Drawable?){
        fullBinding.back.isGone = res == null
        fullBinding.back.setImageDrawable(res)
    }

    fun setLeftIconTint(@ColorInt tint: Int?){
        if(tint == null){
            fullBinding.back.imageTintList = null
        }else{
            fullBinding.back.imageTintList = ColorStateList.valueOf(tint)
        }

    }

    fun setRightIconClick(click: OnClickListener?){
        fullBinding.rightImage.setMultipleClick {
            click?.onClick(it)
        }
    }

    fun setRightIcon(@DrawableRes res: Int){
        setRightIcon(ContextCompat.getDrawable(context, res))
    }

    fun setRightIcon(res: Drawable?){
        fullBinding.rightImage.isGone = res == null
        fullBinding.rightImage.setImageDrawable(res)
    }

    fun setRightIconTint(@ColorInt tint: Int){
        fullBinding.rightImage.imageTintList = ColorStateList.valueOf(tint)
    }

    fun setBarTitle(text: String?){
        fullBinding.titleText.text = text
    }

    fun setTitleTextColor(@ColorInt tint: Int){
        fullBinding.titleText.setTextColor(tint)
    }

    fun setTitleClick(click: OnClickListener?){
        fullBinding.titleText.setMultipleClick { click?.onClick(it) }
    }

    fun setRightMenu(text: String?){
        fullBinding.rightText.isGone = text.isNullOrEmpty()
        fullBinding.rightText.text = text
    }

    fun setRightMenuColor(@ColorInt tint: Int){
        fullBinding.rightText.setTextColor(tint)
    }

    fun setRightMenuClick(click: OnClickListener?){
        fullBinding.rightText.setMultipleClick {
            click?.onClick(it)
        }
    }

    fun setActionbarLineEnable(enable: Boolean){
        fullBinding.actionBarLine.isGone = !enable
    }

    fun setActionbarLineColor(@ColorInt tint: Int){
        fullBinding.actionBarLine.setBackgroundColor(tint)
    }

    fun setFullStatusBarEnable(enable: Boolean){
        fullBinding.root.activity?.setFullEnable(enable)
        setPadding(
            paddingLeft,
            if(enable) context.getStatusBarHeight() else 0,
            paddingRight,
            paddingBottom
        )
    }

    fun setLightStatusModel(enable: Boolean){
        fullBinding.root.activity?.setLightModel(enable)
    }

    fun setNightTheme(){
        setBackgroundColor(Color.BLACK)
        setLeftIconTint(Color.parseColor("#666666"))
        setTitleTextColor(Color.parseColor("#666666"))
        setRightIconTint(Color.parseColor("#666666"))
        setRightMenuColor(Color.parseColor("#666666"))
    }
}
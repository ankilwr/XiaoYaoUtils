package com.shihang.kotlin.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.shihang.kotlin.R
import com.shihang.kotlin.config.GlideCircleTransform


/**
 * Created by Administrator on 2017/4/22.
 */

object GlideUtils {

    fun load(context: Context, url: String, resourceId: Int, imageView: ImageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(resourceId)
                .into(imageView)
    }

    fun loadAvatar(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .transform(GlideCircleTransform(context))
                .placeholder(R.mipmap.ic_avatar)
                .into(imageView)
    }

}

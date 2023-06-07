package com.mellivora.base.glide

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mellivora.base.R
import com.mellivora.base.utils.DisplayUtil
import com.mellivora.base.utils.Utils
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import java.net.URL

fun View.uiIsDestroyed(): Boolean{
    context?.let {
        if(it is Activity){
            return it.isDestroyed
        }
    }
    return false
}

/**
 * 加载图片(默认没有占位图)
 */
fun ImageView.load(path: String?, placeholderSrc: Drawable? = null) {
    if(this.uiIsDestroyed()) return
    val isGif = path?.endsWith(".gif", true) == true
    val glideBuilder = if (isGif) {
        Glide.with(this).asGif().load(path)
    } else {
        Glide.with(this).load(path)
    }
    placeholderSrc?.also {
        glideBuilder
            .apply(RequestOptions().placeholder(it))
            .into(this)
    } ?: run{
        glideBuilder.into(this)
    }
}

/**
 * 加载图片(默认没有占位图)
 */
fun ImageView.load(path: String?, placeholderSrc: Drawable? = null, onSuccess:()->Unit={}) {
    if(this.uiIsDestroyed()) return
    val isGif = path?.endsWith(".gif", true) == true
    val glideBuilder = if (isGif) {
        Glide.with(this).asGif().load(path).addListener(object: RequestListener<GifDrawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                return false
            }
            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onSuccess.invoke()
                return false
            }
        })
    } else {
        Glide.with(this).load(path).addListener(object: RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onSuccess.invoke()
                return false
            }
        })
    }
    placeholderSrc?.also {
        glideBuilder
            .apply(RequestOptions().placeholder(it))
            .into(this)
    } ?: run{
        glideBuilder.into(this)
    }
}

/**
 * 加载图片(默认有个方形占位图)
 */
fun ImageView.loadDefault(path: String?, resourceId: Int? = null) {
    val resource = if(resourceId == 0) null else resourceId
    loadDefaultDrawable(path, resource?.let{ContextCompat.getDrawable(context, it)})
}

/**
 * 加载图片(默认有个方形占位图)
 */
fun ImageView.loadDefaultDrawable(path: String?, resourceDrawable: Drawable? = null) {
    if(this.uiIsDestroyed()) return
    val options = RequestOptions()
        .placeholder(resourceDrawable?: ContextCompat.getDrawable(this.context, R.drawable.base_default_image))
    val isGif = path?.endsWith(".gif", true) == true
    val glideBuilder = if (isGif) {
        Glide.with(this).asGif().load(path)
    } else {
        Glide.with(this).load(path)
    }
    glideBuilder.load(path).apply(options).into(this)
}


fun ImageView.loadCircle(path: String?, placeImage: Int? = null) {
    val resource = if(placeImage == 0) null else placeImage
    loadCircleDrawable(path, resource?.let{ContextCompat.getDrawable(context, it)})
}

fun ImageView.loadCircleDrawable(path: String?, resourceDrawable: Drawable? = null) {
    if(this.uiIsDestroyed()) return
    val options = RequestOptions()
        .placeholder(resourceDrawable?: ContextCompat.getDrawable(this.context, R.drawable.base_default_image_circle))
        .circleCrop()
    val isGif = path?.endsWith(".gif", true) == true
    val glideBuilder = if (isGif) {
        Glide.with(this).asGif().load(path)
    } else {
        Glide.with(this).load(path)
    }
   glideBuilder.apply(options).into(this)
}


fun ImageView.loadRound(path: String?, dp: Float = 5f, resourceId: Int? = null) {
    val resource = if(resourceId == 0) null else resourceId
    loadRoundDrawable(path, dp, resource?.let{ContextCompat.getDrawable(context, it)})
}

fun ImageView.loadRoundDrawable(path: String?, dp: Float = 5f, resourceDrawable: Drawable? = null) {
    if(this.uiIsDestroyed()) return
    var options = RequestOptions()
        .placeholder(resourceDrawable?: ContextCompat.getDrawable(this.context, R.drawable.base_default_image_round))
    val scaleType = when(scaleType){
        ImageView.ScaleType.FIT_CENTER -> FitCenter()
        else -> CenterCrop()
    }
    if(dp > 0f){
        options = options.transform(scaleType, RoundedCorners(DisplayUtil.dp2px(dp)))
    }
    val isGif = path?.endsWith(".gif", true) == true
    val glideBuilder = if (isGif) {
        Glide.with(this).asGif().load(path)
    } else {
        Glide.with(this).load(path)
    }
    glideBuilder.apply(options).into(this)
}


/**
 * 加载drawable目录的资源文件
 */
fun ImageView.loadDrawable(drawableRes: Int, isGif: Boolean = false){
    if(this.uiIsDestroyed()) return
    val glideBuilder = if (isGif) {
        Glide.with(this).asGif().load(drawableRes)
    } else {
        Glide.with(this).load(drawableRes)
    }
    glideBuilder.into(this)
}

/**
 * 可以搞个默认头像
 */
fun ImageView.loadAvatar(path: String?, placeImage: Int = R.drawable.base_default_image_circle) {
    loadCircle(path, placeImage)
}

/**
 * 预加载到手机缓存(预缓存的时候用)
 */
fun loadToCache(path: String?){
    Glide.with(Utils.getApp()).load(path).preload()
}

fun SVGAImageView.loadAssets(fileName: String, onError:()->Unit={}){
    val parser = SVGAParser.shareParser()
    parser.decodeFromAssets(fileName, object : SVGAParser.ParseCompletion {
        override fun onComplete(videoItem: SVGAVideoEntity) {
            val drawable = SVGADrawable(videoItem)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setImageDrawable(drawable)
            startAnimation()
        }
        override fun onError() {
            onError()
        }
    })
}

fun SVGAImageView.loadUrl(url: String, placeholderSrc: Drawable? = null, onError:()->Unit={}){
    if(!url.endsWith(".svga")){
        load(url, placeholderSrc)
        return
    }
    val parser = SVGAParser.shareParser()
    parser.init(this.context)
    parser.decodeFromURL(URL(url), object : SVGAParser.ParseCompletion {
        override fun onComplete(videoItem: SVGAVideoEntity) {
            val drawable = SVGADrawable(videoItem)
            //scaleType = ImageView.ScaleType.FIT_CENTER
            setImageDrawable(drawable)
            startAnimation()
        }
        override fun onError() {
            onError()
        }
    })
}

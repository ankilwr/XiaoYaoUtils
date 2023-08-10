package com.mellivora.base.binding.expansion

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mellivora.base.binding.glide.load
import com.mellivora.base.binding.glide.loadAssets
import com.mellivora.base.binding.glide.loadCircleDrawable
import com.mellivora.base.binding.glide.loadDefaultDrawable
import com.mellivora.base.binding.glide.loadRoundDrawable
import com.mellivora.base.binding.glide.loadUrl
import com.opensource.svgaplayer.SVGAImageView

/**
 * ImageView图片加载Binding
 */
object ImageExpansion {

    /**
     * 无展位图加载-设置了展位图的情况下，转为imageRectUrl加载
     */
    @JvmStatic
    @BindingAdapter("bindImageUrl", "bindPlaceholderSrc", requireAll = false)
    fun imageUrl(imageView: ImageView, path: String?, placeholderSrc: Drawable? = null) {
        if(placeholderSrc != null){
            imageView.loadDefaultDrawable(path, placeholderSrc)
        }else{
            imageView.load(path)
        }
    }

    @JvmStatic
    @BindingAdapter("bindImageRectUrl", "bindPlaceholderSrc", requireAll = false)
    fun imageRectUrl(imageView: ImageView, path: String?, placeholderSrc: Drawable? = null) {
        imageView.loadDefaultDrawable(path, placeholderSrc)
    }

    @JvmStatic
    @BindingAdapter("bindImageRoundUrl", "bindImageRoundSize", "bindPlaceholderSrc", requireAll = false)
    fun imageRoundUrl(imageView: ImageView, path: String?, round: Float? = 5f, placeholderSrc: Drawable? = null) {
        imageView.loadRoundDrawable(path, round?:5f, placeholderSrc)
    }

    @JvmStatic
    @BindingAdapter("bindImageCircleUrl", "bindPlaceholderSrc", requireAll = false)
    fun imageCircleUrl(imageView: ImageView, path: String?, placeholderSrc: Drawable? = null) {
        imageView.loadCircleDrawable(path, placeholderSrc)
    }

    @JvmStatic
    @BindingAdapter("bindSvgaImageUrl", "bindPlaceholderSrc", requireAll = false)
    fun svgaImageLoad(imageView: SVGAImageView, path: String?, placeholderSrc: Drawable? = null) {
        imageView.loadUrl(path?:"", placeholderSrc)
    }

    @JvmStatic
    @BindingAdapter("bindSvgaAssets", requireAll = false)
    fun svgaImageLoad(imageView: SVGAImageView, assetsPath: String?) {
        imageView.loadAssets(assetsPath?:"")
    }


}
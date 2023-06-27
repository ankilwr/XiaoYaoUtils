package com.mellivora.base.ui.widget

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.mellivora.base.glide.loadAssets
import com.mellivora.base.glide.loadUrl
import com.opensource.svgaplayer.SVGAImageView

/**
 * Theme主题
 */
@Composable
fun ComposeTheme(){

}


/**
 * SVGA IMAGE
 */
@Composable
fun SvgaImage(
    modifier: Modifier = Modifier,
    placeholderSrc: Drawable? = null,
    assetsFileName: String? = null,
    httpUrl: String? = null,
    onError:(()->Unit)? = null
){
    AndroidView(
        factory = { SVGAImageView(it) },
        modifier = modifier,
        update = {
            if(assetsFileName != null){
                it.loadAssets(assetsFileName){ onError?.invoke() }
            }else{
                it.loadUrl(httpUrl ?: "", placeholderSrc){ onError?.invoke() }
            }
        }
    )
}


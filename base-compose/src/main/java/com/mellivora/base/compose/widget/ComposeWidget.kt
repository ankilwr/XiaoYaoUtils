package com.mellivora.base.compose.widget

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import java.net.URL

/**
 * SVGA IMAGE
 */
@Composable
fun SvgaImage(
    modifier: Modifier = Modifier,
    placeholderSrc: Drawable? = null,
    assetsFileName: String? = null,
    httpUrl: String? = null
) {
    if(httpUrl?.endsWith(".svga") == true){
        //渲染SVGA
        AndroidView(
            factory = { SVGAImageView(it) },
            modifier = modifier,
            update = {
                it.setImageDrawable(placeholderSrc)
                val parser = SVGAParser.shareParser()
                parser.init(it.context)
                val parserCallback = object : SVGAParser.ParseCompletion {
                    override fun onComplete(videoItem: SVGAVideoEntity) {
                        val drawable = SVGADrawable(videoItem)
                        it.setImageDrawable(drawable)
                        it.startAnimation()
                    }
                    override fun onError() {}
                }
                if (assetsFileName != null) {
                    parser.decodeFromAssets(assetsFileName, parserCallback)
                } else {
                    parser.decodeFromURL(URL(httpUrl), parserCallback)
                }
            }
        )
    }else{
        //渲染普通的图片
        AsyncImage(
            contentDescription = "",
            model = ImageRequest.Builder(LocalContext.current)
                .data(httpUrl)
                .placeholder(placeholderSrc)
                .error(placeholderSrc)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

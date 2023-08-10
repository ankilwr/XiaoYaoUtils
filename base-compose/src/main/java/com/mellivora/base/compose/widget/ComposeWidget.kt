package com.mellivora.base.compose.widget

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mellivora.base.R
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import java.net.URL

/**
 * Theme主题
 */
@Composable
fun DefaultAppTheme(
    colors: Colors = MaterialTheme.colors,
    typography: Typography = MaterialTheme.typography,
    shapes: Shapes = MaterialTheme.shapes,
    title: String? = null,
    rightText: String? = null,
    onBackClick: (() -> Unit)? = null,
    onTitleClick: (() -> Unit)? = null,
    onRightTextClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {

    MaterialTheme(colors, typography, shapes){
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
        ) {
            Column(Modifier
                .background(MaterialTheme.colors.primary)
            ) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.base_icon_back_white),
                        contentDescription = "返回",
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .clickable { onBackClick?.invoke() }
                            .padding(start = 10.dp, end = 10.dp),
                        tint = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
                    )
                    if (!title.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable { onTitleClick?.invoke() },
                            textAlign = TextAlign.Center,
                            text = title,
                            color = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
                            fontSize = 18.sp
                        )
                    }
                    if (!rightText.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier
                                .fillMaxHeight()
                                .align(Alignment.CenterEnd)
                                .clickable { onRightTextClick?.invoke() },
                            text = rightText,
                            color = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            content()
        }
    }

}


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


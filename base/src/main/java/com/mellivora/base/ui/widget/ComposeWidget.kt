package com.mellivora.base.ui.widget

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.statusBarsHeight
import com.mellivora.base.R
import com.mellivora.base.glide.loadAssets
import com.mellivora.base.glide.loadUrl
import com.opensource.svgaplayer.SVGAImageView

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
            Column(Modifier.background(MaterialTheme.colors.primary)) {
                Spacer(Modifier.statusBarsHeight())
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)) {
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
    httpUrl: String? = null,
    onError: (() -> Unit)? = null
) {
    AndroidView(
        factory = { SVGAImageView(it) },
        modifier = modifier,
        update = {
            if (assetsFileName != null) {
                it.loadAssets(assetsFileName) { onError?.invoke() }
            } else {
                it.loadUrl(httpUrl ?: "", placeholderSrc) { onError?.invoke() }
            }
        }
    )
}


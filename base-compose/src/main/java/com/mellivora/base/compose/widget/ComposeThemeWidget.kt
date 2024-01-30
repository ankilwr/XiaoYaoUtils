package com.mellivora.base.compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mellivora.base.R
import com.mellivora.base.compose.remember.rememberOnDefaultBackClick

/**
 * Theme主题
 */
@Composable
fun DefaultAppTheme(
    modifier: Modifier = Modifier.fillMaxWidth(),
    theme: MaterialTheme = MaterialTheme,
    title: String? = null,
    onTitleClick: (() -> Unit)? = null,
    actionbarCompose: (@Composable BoxScope.() -> Unit)? = null
) {
    Column(modifier.background(theme.colors.primary)) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            actionbarCompose?.invoke(this)
            if (!title.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { onTitleClick?.invoke() },
                    textAlign = TextAlign.Center,
                    text = title,
                    color = if (theme.colors.isLight) Color.White else Color.Black,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun BackTheme(
    theme: MaterialTheme = MaterialTheme,
    title: String? = null,
    onTitleClick: (() -> Unit)? = null,
    onBackClick:() -> Unit = rememberOnDefaultBackClick()
){
    DefaultAppTheme(
        theme = theme,
        title = title,
        onTitleClick = onTitleClick,
    ){
        Icon(
            painter = painterResource(id = R.drawable.base_icon_back_white),
            contentDescription = "返回",
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .clickable { onBackClick.invoke() }
                .padding(start = 10.dp, end = 10.dp),
            tint = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
        )
    }
}





@Preview(showBackground = true)
@Composable
private fun DefaultThemePreview() {
    DefaultAppTheme(title = "Compose示例")
}

@Preview(showBackground = true)
@Composable
private fun BackThemePreview() {
    BackTheme(title = "Compose示例")
}


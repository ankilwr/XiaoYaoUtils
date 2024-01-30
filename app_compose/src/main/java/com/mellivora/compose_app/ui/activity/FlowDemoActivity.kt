package com.mellivora.compose_app.ui.activity

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mellivora.base.compose.attr.LightThemeColors
import com.mellivora.base.compose.attr.otherColor
import com.mellivora.base.compose.ui.activity.BaseComposeActivity
import com.mellivora.base.compose.widget.DefaultAppTheme
import com.mellivora.base.expansion.createIntent
import com.mellivora.base.expansion.setFullEnable

class FlowDemoActivity : BaseComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullEnable(true)
    }

    @Preview(
        showBackground = true,
        widthDp = 360,
        heightDp = 640,
    )
    @Composable
    override fun InitMainComposeView() {
        MaterialTheme(
            colors = LightThemeColors
        ){
            Column(Modifier.fillMaxSize()) {
                DefaultAppTheme(title = "Compose示例")
                ContentView()
            }
        }
    }

    @Composable
    fun ContentView() {
    }
}


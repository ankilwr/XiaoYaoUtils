package com.mellivora.compose_app.ui.activity

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mellivora.base.compose.attr.LightThemeColors
import com.mellivora.base.compose.attr.otherColor
import com.mellivora.base.compose.ui.activity.BaseComposeActivity
import com.mellivora.base.compose.widget.DefaultAppTheme
import com.mellivora.base.expansion.createIntent
import com.mellivora.base.expansion.setFullEnable

class MainActivity : BaseComposeActivity() {
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
            ContentView()
        }
    }
}

@Composable
fun ContentView() {
    val context = LocalContext.current
    Column{
        DefaultAppTheme(title = "Compose示例")
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .verticalScroll(rememberScrollState())
        ){
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val intent = context.createIntent(RepositoryListActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "github仓库列表",
                    color = MaterialTheme.colors.otherColor.white
                )
            }
        }
    }
}
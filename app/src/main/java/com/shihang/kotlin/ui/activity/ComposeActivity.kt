package com.shihang.kotlin.ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mellivora.base.expansion.setFullEnable
import com.mellivora.base.expansion.showToast
import com.mellivora.base.ui.widget.DefaultAppTheme
import com.mellivora.base.ui.widget.LinkifyText
import com.mellivora.base.ui.widget.attr.ComposeColor
import com.mellivora.base.ui.widget.attr.LightThemeColors
import com.mellivora.base.ui.widget.attr.otherColor
import com.shihang.kotlin.R
import com.shihang.kotlin.bean.GithubRepositoryBean
import com.shihang.kotlin.vm.RepositoryListViewModel
import kotlinx.coroutines.launch

class ComposeActivity : AppCompatActivity() {

    private val viewModel: RepositoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullEnable(true)
        setContent {
            Surface() {
                ComposeMainContentView()
            }
        }
        viewModel.loadListData(true, isPullAction = false)
    }


    @Preview("示例页面")
    @Composable
    private fun ComposeMainContentView() {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val dataList = viewModel.dataList.observeAsState()

        DefaultAppTheme(
            colors = LightThemeColors,
            title = "标题",
            onBackClick = {
                if (context is Activity) {
                    context.finish()
                }
            }
        ) {
            LazyColumn {
                val list = dataList.value ?: return@LazyColumn
                itemsIndexed(list) { _, item ->
                    RepositoryItem(item)
                }
            }
        }
    }

    @Preview
    @Composable
    fun RepositoryItem(data: GithubRepositoryBean?) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colors.otherColor.white)
                .padding(15.dp, 15.dp, 15.dp, 0.dp)
        ) {
            Row {
                AsyncImage(
                    contentDescription = "头像",
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data?.owner?.avatar_url)
                        .placeholder(R.drawable.base_default_image_circle)
                        .error(R.drawable.base_default_image_circle)
                        .build(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp, 35.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = data?.owner?.login ?: "wahaha",
                    color = MaterialTheme.colors.otherColor.textColor333,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            LinkifyText(
                text = "${data?.name}\n${data?.description}\n${data?.html_url}",
                color = MaterialTheme.colors.otherColor.textColor666,
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
            )

            Divider(
                modifier = Modifier
                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(MaterialTheme.colors.otherColor.dividerColor)
            )
        }
    }


}


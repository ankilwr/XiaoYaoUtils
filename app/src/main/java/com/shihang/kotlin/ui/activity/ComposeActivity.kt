package com.shihang.kotlin.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mellivora.base.compose.attr.LightThemeColors
import com.mellivora.base.compose.attr.otherColor
import com.mellivora.base.compose.attr.rememberOnDefaultBackClick
import com.mellivora.base.compose.ui.activity.BaseComposeActivity
import com.mellivora.base.compose.widget.DefaultAppTheme
import com.mellivora.base.compose.widget.LinkifyText
import com.mellivora.base.compose.widget.MultiStateWidget
import com.mellivora.base.expansion.setFullEnable
import com.mellivora.base.expansion.showToast
import com.mellivora.base.state.LoadingState
import com.mellivora.base.state.PullState
import com.shihang.kotlin.R
import com.shihang.kotlin.bean.GithubRepositoryBean
import com.shihang.kotlin.bean.Owner
import com.shihang.kotlin.vm.RepositoryListViewModel

class ComposeActivity : BaseComposeActivity() {

    private val viewModel: RepositoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullEnable(true)
    }

    @Composable
    override fun InitMainComposeView() {
        ComposeMainContentView(viewModel)
    }

    override fun onLazyLoad() {
        viewModel.loadListData(true, isPullAction = false)
    }

    @Preview("示例页面")
    @Composable
    private fun ComposeMainContentView(
        @PreviewParameter(ComposeMainViewModelProvider::class,1)
        viewModel: RepositoryListViewModel,
    ) {
        //val scope = rememberCoroutineScope()
        val rememberPullState = viewModel.pullState
        val rememberStateList = viewModel.dataStateList

        println("测试测试: ComposeMainContentView()")

        DefaultAppTheme(
            colors = LightThemeColors,
            title = rememberStateList.getOrNull(0)?.owner?.login ?: "标题",
            onBackClick = rememberOnDefaultBackClick()
        ) {
            println("测试测试: DefaultAppTheme()")
            MultiStateWidget(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                state = rememberPullState.value ?: PullState(),
                isDataEmpty = rememberStateList.isEmpty(),
                onReloadClick = { showToast("哇哈哈") }
            ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    println("测试测试: DefaultAppTheme.LazyColumn()")
                    itemsIndexed(
                        items = rememberStateList,
                        contentType = { _, it -> it::class }
                    ) { _, item ->
                        RepositoryItem(item){
                            showToast(item.name)
                        }
                    }
                }
            }
        }
    }


    @Preview("列表")
    @Composable
    private fun RepositoryItem(
        @PreviewParameter(GithubRepositoryDataProvider::class,1)
        data: GithubRepositoryBean,
        onItemClick:(()-> Unit)? = null
    ) {

        println("测试测试: DefaultAppTheme.RepositoryItem()")

//        val colors = mutableListOf(
//           Color.Blue, Color.DarkGray, Color.Red, Color.Yellow, Color.Cyan
//        )
        Column(
            modifier = Modifier
                .clickable { onItemClick?.invoke() }
                .background(MaterialTheme.colors.otherColor.white)
                .padding(15.dp, 15.dp, 15.dp, 0.dp)
        ) {

            Row {
                AsyncImage(
                    contentDescription = "头像",
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data.owner?.avatar_url)
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
                    text = data.owner?.login ?: "",
                    color = MaterialTheme.colors.otherColor.textColor333,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            LinkifyText(
                text = "${data.name}\n${data.description}\n${data.html_url}",
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

private class ComposeMainViewModelProvider : PreviewParameterProvider<RepositoryListViewModel> {
    private val dataProvider = GithubRepositoryDataProvider()
    override val values: Sequence<RepositoryListViewModel>
        get() = listOf(
            RepositoryListViewModel().apply {
                pullSuccess(isRefresh = true, isPull = true, false)
                dataStateList.addAll(dataProvider.values)
                dataStateList.addAll(dataProvider.values)
                dataStateList.addAll(dataProvider.values)
            }
        ).asSequence()
}

private class GithubRepositoryDataProvider : PreviewParameterProvider<GithubRepositoryBean> {
    override val values: Sequence<GithubRepositoryBean>
        get() = listOf(
            GithubRepositoryBean(
                name = "SmartRefreshLayout",
                owner = Owner(login = "ankilwr"),
                description = "下拉刷新、上拉加载、二级刷新、淘宝二楼、RefreshLayout、 OverScroll, Android智能下拉刷新框架，支持越界回弹、越界拖动，具有极强的扩展性，集成了几十种炫酷的Header和Footer",
                html_url = "https://aithub.com/ankilwrSmartRefreshLavout"
            )
        ).asSequence()
}




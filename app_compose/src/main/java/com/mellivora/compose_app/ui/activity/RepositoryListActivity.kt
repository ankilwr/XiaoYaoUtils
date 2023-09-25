package com.mellivora.compose_app.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mellivora.base.compose.attr.LightThemeColors
import com.mellivora.base.compose.attr.otherColor
import com.mellivora.base.compose.ui.activity.BaseComposeActivity
import com.mellivora.base.compose.widget.BackTheme
import com.mellivora.base.compose.widget.LinkifyText
import com.mellivora.base.compose.widget.MultiStateWidget
import com.mellivora.base.expansion.setFullEnable
import com.mellivora.compose_app.R
import com.mellivora.compose_app.bean.GithubRepositoryBean
import com.mellivora.compose_app.bean.Owner
import com.mellivora.compose_app.vm.RepositoryListViewModel

@OptIn(ExperimentalFoundationApi::class)
class RepositoryListActivity : BaseComposeActivity() {

    private val viewModel: RepositoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullEnable(true)
    }

    @Composable
    override fun InitMainComposeView() {
        ThemePreview(viewModel)
    }

    @Preview(
        showBackground = true,
        widthDp = 360,
        heightDp = 640
    )
    @Composable
    private fun ThemePreview(
        @PreviewParameter(RepositoryListViewModelProvider::class,1)
        viewModel: RepositoryListViewModel
    ){
        MaterialTheme(
            colors = LightThemeColors
        ){
            ComposeMainContentView(viewModel)
        }
    }

    @Composable
    private fun ComposeMainContentView(viewModel: RepositoryListViewModel) {

        val rememberPullState = viewModel.pullState.collectAsState()
        val rememberStateList = remember { viewModel.dataStateList }

        LaunchedEffect(Unit){
            if(viewModel.isNoneState()){
                viewModel.loadListData(true, isPull = false)
            }
        }

        Column(Modifier.fillMaxSize()) {
            BackTheme(
                title = viewModel.dataStateList.getOrNull(0)?.owner?.login ?: "",
            )
            MultiStateWidget(
                modifier = Modifier.fillMaxSize(),
                state = rememberPullState.value,
                isDataEmpty = rememberStateList.isEmpty(),
                onReloadClick = { viewModel.loadListData(isRefresh = true, false) }
            ){
                LazyColumn(Modifier.fillMaxSize()) {
                    itemsIndexed(
                        items = rememberStateList,
                        key = { index, it -> it.id ?: "$index" },
                        contentType = { _, it -> it::class }
                    ) { position, item ->
                        RepositoryItem(
                            data = item,
                            modifier = Modifier.animateItemPlacement()
                        ){
                            viewModel.removePosition(position)
                        }
                    }
                }
            }
        }
    }


    @Preview(
        showBackground = true,
        widthDp = 360
    )
    @Composable
    private fun RepositoryItem(
        @PreviewParameter(GithubRepositoryDataProvider::class,1)
        data: GithubRepositoryBean,
        modifier: Modifier = Modifier,
        onItemClick:(()-> Unit)? = null
    ) {
        Column(
            modifier = modifier
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


private class RepositoryListViewModelProvider : PreviewParameterProvider<RepositoryListViewModel> {
    private val dataProvider = GithubRepositoryDataProvider()
    override val values: Sequence<RepositoryListViewModel>
        get() = listOf(
            RepositoryListViewModel().apply {
                dataStateList.addAll(dataProvider.values)
                dataStateList.addAll(dataProvider.values)
                dataStateList.addAll(dataProvider.values)
                loadingSuccess()
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
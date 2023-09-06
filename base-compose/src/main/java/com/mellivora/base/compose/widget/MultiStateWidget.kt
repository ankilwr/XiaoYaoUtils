package com.mellivora.base.compose.widget

import android.graphics.drawable.AnimationDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.mellivora.base.compose.R
import com.mellivora.base.exception.ErrorStatus
import com.mellivora.base.state.LoadingState
import com.mellivora.base.state.PullState

/**
 * 多状态布局
 */
@Composable
fun MultiStateWidget(
    modifier: Modifier = Modifier,
    state: PullState,
    onReloadClick:() -> Unit,
    isDataEmpty: Boolean = false,
    loadingText: String? = stringResource(R.string.base_status_loading),
    emptyText: String? = stringResource(R.string.base_status_empty),
    loadingWidget: @Composable (String?) -> Unit = {
        ComposeLoadingWidget(loadingText)
    },
    emptyWidget: @Composable (String?, onReloadClick:() -> Unit) -> Unit = { emptyMsg, reloadClick ->
        ComposeEmptyWidget(emptyMsg, reloadClick)
    },
    errorWidget: @Composable (String?, onReloadClick:() -> Unit) -> Unit = { errorMsg, reloadClick ->
        ComposeErrorWidget(errorMsg, reloadClick)
    },
    networkErrorWidget: @Composable (String?, onReloadClick:() -> Unit) -> Unit = { networkErrorMsg, reloadClick ->
        ComposeNetworkErrorWidget(networkErrorMsg, reloadClick)
    },
    contentWidget: @Composable () -> Unit,
) {

    println("测试测试: MultiStateWidget() state:${state.loadingState.name}")

    Box(modifier = modifier){

        println("测试测试: MultiStateWidget().Box()")

        when(state.loadingState){
            LoadingState.LOADING -> {
                if(state.isRefresh && !state.isPull){
                    loadingWidget(loadingText)
                }
            }
            LoadingState.ERROR -> {
                if(state.code == ErrorStatus.NETWORK_ERROR){
                    networkErrorWidget(state.message, onReloadClick)
                }else{
                    errorWidget(state.message, onReloadClick)
                }
            }
            LoadingState.SUCCESS -> {
                if(isDataEmpty){
                    emptyWidget(emptyText, onReloadClick)
                }else{
                    contentWidget()
                }
            }
            else -> {
                contentWidget()
            }
        }
    }
}

/**
 * 正在加载...
 */
@Preview(
    name = "正在加载...",
    showBackground = true,
    backgroundColor = 0xffffffff,
    widthDp = 320,
    heightDp = 640
)
@Composable
fun ComposeLoadingWidget(
    @PreviewParameter(StringProvider::class,1)
    message: String?
) {

//    val context = LocalContext.current
//    val animationDrawable = remember {
//        ContextCompat.getDrawable(context, R.drawable.base_anim_loading)
//        AnimationDrawable().apply {
//            addFrame(resour, 100)
//            addFrame(ContextCompat.getDrawable(LocalContext.current, R.drawable.frame2)!!, 100)
//            addFrame(ContextCompat.getDrawable(LocalContext.current, R.drawable.frame3)!!, 100)
//            isOneShot = false
//        }
//    }
//    val currentIndex = remember { mutableStateOf(0) }
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(animationDrawable.getDuration(currentIndex.value).toLong())
//            withContext(Dispatchers.Main) {
//                currentIndex.value = (currentIndex.value + 1) % animationDrawable.numberOfFrames
//            }
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.333f))
//        Image(
//            //painter = rememberDrawablePainter(drawable = animationDrawable.getFrame(currentIndex.value)),
////            painter = rememberDrawablePainter(drawable = animationDrawable),
//            painter = painterResource(R.drawable.base_anim_loading),
//            contentDescription = null,
//            modifier = Modifier.size(40.dp)
//        )
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp)
        )
        if(message?.isNotEmpty() == true){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(0.666f))
    }
}


/**
 * 数据为空...
 */
@Preview(
    name = "数据为空...",
    showBackground = true,
    backgroundColor = 0xffffffff,
    widthDp = 320,
    heightDp = 640
)
@Composable
fun ComposeEmptyWidget(
    @PreviewParameter(StringProvider::class,1)
    message: String?,
    onReloadClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable(
                onClick = { onReloadClick?.invoke() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.333f))
        Image(
            painter = painterResource(id = R.drawable.base_status_empty_default),
            contentDescription = ""
        )
        if(message?.isNotEmpty() == true){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(0.666f))
    }
}


/**
 * 加载错误...
 */
@Preview(
    name = "加载错误...",
    showBackground = true,
    backgroundColor = 0xffffffff,
    widthDp = 320,
    heightDp = 640
)
/**
 * 加载错误...
 */
@Composable
fun ComposeErrorWidget(
    @PreviewParameter(StringProvider::class,1)
    errorMsg: String?,
    onReloadClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable(
                onClick = { onReloadClick?.invoke() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(0.333f))
        Image(
            painter = painterResource(id = R.drawable.base_status_error_default),
            contentDescription = ""
        )
        if(errorMsg?.isNotEmpty() == true){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMsg,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(0.666f))
    }
}


/**
 * 网络异常
 */
@Preview(
    name = "网络异常...",
    showBackground = true,
    backgroundColor = 0xffffffff,
    widthDp = 320,
    heightDp = 640
)
@Composable
fun ComposeNetworkErrorWidget(
    @PreviewParameter(StringProvider::class,1)
    networkErrorMsg: String?,
    onReloadClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable(
                onClick = { onReloadClick?.invoke() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.333f))
        Image(
            painter = painterResource(id = R.drawable.base_status_network_error_default),
            contentDescription = ""
        )
        if(networkErrorMsg?.isNotEmpty() == true){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = networkErrorMsg,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(0.666f))
    }
}


private class StringProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = listOf("测试测试").asSequence()
}

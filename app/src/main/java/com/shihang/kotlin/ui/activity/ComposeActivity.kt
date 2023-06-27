package com.shihang.kotlin.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mellivora.base.ui.widget.attr.ComposeFont
import com.mellivora.base.ui.widget.attr.DarkThemeColors

class ComposeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface() {
                contentView()
            }
        }
    }
}

@Preview
@Composable
fun contentView(){
    MaterialTheme(
        colors = DarkThemeColors
    ) {
        Column(Modifier.background(
            color = MaterialTheme.colors.background,
            shape = MaterialTheme.shapes.medium)
        ) {
            Text(
                text = "文字1 Hello primary",
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                
                fontWeight = FontWeight.W100
            )
            Text(
                text = "文字1 Hello primary",
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                
                fontWeight = FontWeight.W200
            )
            Text(
                text = "文字1 Hello primary",
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                
                fontWeight = FontWeight.W300
            )
            Text(
                text = "文字1 Hello primary",
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                
                fontWeight = FontWeight.W400
            )
            Text(
                text = "文字1 Hello primary",
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                
                fontWeight = FontWeight.W500
            )
            Text(
                text = "文字1 Hello primary",
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                
                fontWeight = FontWeight.W600
            )
            Text(
                text = "文字1 Hello primary",
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                
                fontWeight = FontWeight.W700
            )
            Text(
                text = "文字1 Hello primary",
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,

                fontWeight = FontWeight.W800
            )










            Text(
                text = "文字1 Hello primary",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h1.copy(
                    fontWeight = FontWeight(700)
                ),
                fontFamily = ComposeFont.SourceHanSansNormal
            )

            Text(
                text = "文字2 Hello primaryVariant",
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h2
            )

            Text(
                text = "文字3 Hello error",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.h3
            )

            Text(
                text = "文字4 Hello secondary",
                color = MaterialTheme.colors.secondary,
                style = MaterialTheme.typography.body1
            )

            Text(
                text = "文字5 Hello secondaryVariant",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.body2
            )
        }
//
//        Row(modifier = Modifier
//            .fillMaxWidth()
//        ) {
//            Text(text = "返回")
//            Box(
//                modifier = Modifier.weight(1f),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "标题"
//                )
//            }
//            Text(text = "确定")
//        }
//        Column(modifier = Modifier
//            .fillMaxHeight()
//        ){
//        }
    }


//    Column(modifier = Modifier
//        .fillMaxWidth()
//        .fillMaxHeight()
//    ) {
//        ConstraintLayout(modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp)
//            .background(Color.LightGray)
//        ){
//            val (tvTitle, ivBack) = createRefs()
//            Text("Hello Word", modifier = Modifier.constrainAs(tvTitle) {
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//                top.linkTo(parent.top)
//                bottom.linkTo(parent.bottom)
//            })
//            SvgaImage(modifier = Modifier
//                .constrainAs(ivBack) {
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    top.linkTo(tvTitle.bottom)
//                    bottom.linkTo(parent.bottom)
//                }
//                .width(50.dp)
//                .height(50.dp)
//                .background(Color(0xffe2e2e2)))
//        }
//
//        Column(modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .background(Color.White)
//        ) {
//
//        }
//    }
}



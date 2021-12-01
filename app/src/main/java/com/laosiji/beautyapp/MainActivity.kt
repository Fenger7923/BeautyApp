package com.laosiji.beautyapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.glide.rememberGlidePainter
import com.laosiji.beautyapp.ui.theme.BeautyAppTheme
import com.laosiji.beautyapp.ui.view.LoadingPage
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        setContent {
            val mainViewModel: MainViewModel = viewModel()
            var wholePicSize by remember { mutableStateOf(0) }
            BeautyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state by mainViewModel.stateLiveData.observeAsState(State.Loading)
                    val newsModel by mainViewModel.picLiveData.observeAsState(emptyList())
                    LoadingPage(state = state,
                        loadInit = {
                            mainViewModel.getPicList()
                        }, contentView = {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.padding(5.dp))
                                Text(text = "当前有 $wholePicSize 张图片")
                                Spacer(modifier = Modifier.padding(5.dp))
                                Row(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .fillMaxWidth()
                                ) {
                                    SizeClickableText(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(1f)
                                            .background(
                                                color = Color.Green,
                                                shape = RoundedCornerShape(50.dp)
                                            ), addRange = 1
                                    ) {
                                        wholePicSize += 1
                                        veryNeedToRefresh(wholePicSize, newsModel, mainViewModel)
                                    }
                                    SizeClickableText(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(1f)
                                            .background(
                                                color = Color.Green,
                                                shape = RoundedCornerShape(50.dp)
                                            ), addRange = 3
                                    ) {
                                        wholePicSize += 3
                                        veryNeedToRefresh(wholePicSize, newsModel, mainViewModel)
                                    }
                                }
                                Spacer(modifier = Modifier.padding(5.dp))
                                LazyColumn {
                                    itemsIndexed(
                                        newsModel.subList(0, min(wholePicSize, newsModel.size))
                                    ) { index, item ->
                                        Text(
                                            text = "第${index + 1}张",
                                            modifier = Modifier
                                                .wrapContentHeight()
                                                .fillMaxWidth()
                                                .padding(top = 5.dp, bottom = 5.dp),
                                            textAlign = TextAlign.Center
                                        )
                                        Image(
                                            painter = rememberGlidePainter(item.thumb),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .wrapContentHeight()
                                                .fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun veryNeedToRefresh(
        wholePicSize: Int,
        newsModel: List<Photo>,
        mainViewModel: MainViewModel
    ) {
        if (wholePicSize == newsModel.size) {
            mainViewModel.getPicList()
            Toast
                .makeText(
                    this@MainActivity,
                    "冲太快了～，缓一缓",
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    @Composable
    fun SizeClickableText(modifier: Modifier, addRange: Int, clickListener: () -> Unit) {
        Box(modifier = modifier
            .clickable {
                clickListener.invoke()
            }) {
            Text(
                text = "来冲$addRange 个",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

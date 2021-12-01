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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.glide.rememberGlidePainter
import com.laosiji.beautyapp.ui.theme.BeautyAppTheme
import com.laosiji.beautyapp.ui.view.LoadingPage
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                            Column(Modifier.fillMaxSize()) {
                                Text(text = "当前有 $wholePicSize 张图片")
                                Row(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f)
                                        .background(
                                            color = Color.Green,
                                            shape = RoundedCornerShape(50.dp)
                                        )
                                        .clickable {
                                            wholePicSize += 1
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
                                        }) {
                                        Text(
                                            text = "来一个",
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                    Box(modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f)
                                        .background(
                                            color = Color.Green,
                                            shape = RoundedCornerShape(50.dp)
                                        )
                                        .clickable {
                                            wholePicSize += 3
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
                                        }) {
                                        Text(
                                            text = "来三个",
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                                LazyColumn {
                                    itemsIndexed(
                                        newsModel.subList(0, min(wholePicSize, newsModel.size))
                                    ) { index, item ->
                                        Text(text = "$index : ${item.title}")
                                        Image(
                                            painter = rememberGlidePainter(item.thumb),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(200.dp)
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


}

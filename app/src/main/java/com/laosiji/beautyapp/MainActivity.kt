package com.laosiji.beautyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.laosiji.beautyapp.ui.theme.BeautyAppTheme
import com.laosiji.beautyapp.ui.view.LoadingPage

@OptIn(ExperimentalPagerApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        setContent {
            val mainViewModel: MainViewModel = viewModel()

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
                            VerticalPager(
                                count = newsModel.size,
                                contentPadding = PaddingValues(vertical = 100.dp)
                            ) { page ->
                                Image(
                                    painter = rememberGlidePainter(newsModel[page].link),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

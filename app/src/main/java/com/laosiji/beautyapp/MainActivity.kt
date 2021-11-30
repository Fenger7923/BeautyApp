package com.laosiji.beautyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.Modifier
import com.laosiji.beautyapp.ui.theme.BeautyAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.laosiji.beautyapp.ui.view.LoadingPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                            Column(Modifier.fillMaxSize()) {
                                LazyColumn {
                                    itemsIndexed(newsModel) { _, item ->
                                        Text(text = item.title)
                                    }
                                }
                            }
                        })
                }
            }
        }
    }
}

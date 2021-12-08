package com.laosiji.beautyapp

import androidx.lifecycle.MutableLiveData
import com.laosiji.beautyapp.network.HttpController
import java.util.*
import kotlin.random.Random

class MainViewModel: BaseViewModel() {
    val picLiveData = MutableLiveData<List<Photo>>()

    fun getPicList() {
        launch {
            val photo = HttpController.getPhotoFormServer()
            Collections.shuffle(photo) // 乱序
            picLiveData.value = photo
        }
    }
}
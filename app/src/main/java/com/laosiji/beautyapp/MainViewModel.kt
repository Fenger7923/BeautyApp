package com.laosiji.beautyapp

import androidx.lifecycle.MutableLiveData
import com.laosiji.beautyapp.network.HttpController

/**
 * @author fengerzhang
 * @date 2021/11/30 16:36
 */
class MainViewModel: BaseViewModel() {
    val picLiveData = MutableLiveData<List<Photo>>()

    fun getPicList() {
        launch {
            val photo = HttpController.getPhotoFormServer(1000)
            picLiveData.value = photo
        }
    }
}
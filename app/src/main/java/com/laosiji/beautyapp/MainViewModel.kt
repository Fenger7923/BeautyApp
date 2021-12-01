package com.laosiji.beautyapp

import androidx.lifecycle.MutableLiveData
import com.laosiji.beautyapp.network.HttpController
import kotlin.random.Random

/**
 * @author fengerzhang
 * @date 2021/11/30 16:36
 */
class MainViewModel: BaseViewModel() {
    val picLiveData = MutableLiveData<List<Photo>>()

    fun getPicList() {
        launch {
            val randomPage = Random.nextInt(1, 120)
            val photo = HttpController.getPhotoFormServer(randomPage)
            val value: MutableList<Photo> = (picLiveData.value ?: emptyList()).toMutableList()
            value.addAll(photo)
            picLiveData.value = value
        }
    }
}
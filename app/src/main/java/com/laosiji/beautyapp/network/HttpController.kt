package com.laosiji.beautyapp.network

import com.laosiji.beautyapp.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object HttpController {
    private const val BASE_URL = "http://www.xiuren.org/"
    private const val PIC_URL = "https://cdn.jsdelivr.net/gh/Fenger7923/PictureBed@master/PictureInXiuRen"
    private const val PIC_LIST_URL= "https://cdn.jsdelivr.net/gh/Fenger7923/PictureBed@master/pictureList.txt"

    suspend fun getPhotoFormServer(): List<Photo> {
        val resultPhoto: MutableList<Photo> = mutableListOf()
        withContext(Dispatchers.IO) {
            val picList = readFromFile(PIC_LIST_URL)
            picList.forEach { picName ->
                resultPhoto.add(Photo("$PIC_URL/$picName", picName))
            }
        }
        return resultPhoto
    }

    private suspend fun readFromFile(url: String): List<String> {
        val result: MutableList<String> = mutableListOf()
        withContext(Dispatchers.IO) {
            val fileUrl = URL(url)

            val input = BufferedReader(InputStreamReader(fileUrl.openStream()))
            var item: String?
            while (input.readLine().also { item = it } != null) {
                item?.let { result.add(it) }
            }
        }
        return result
    }
}
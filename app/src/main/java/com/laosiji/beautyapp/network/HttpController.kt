package com.laosiji.beautyapp.network

import com.laosiji.beautyapp.Photo
import com.laosiji.beautyapp.pachong.readFromFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URI

object HttpController {
    private const val BASE_URL = "http://www.xiuren.org/"
    private const val PIC_URL = "https://cdn.jsdelivr.net/gh/Fenger7923/PictureBed@master/PictureInXiuRen"
    private const val PIC_LIST_URL= "https://cdn.jsdelivr.net/gh/Fenger7923/PictureBed@main/pictureList.txt"

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

    suspend fun getPhotoFormServerInJava(page: Int) : List<Photo> {
        val resultPhoto: MutableList<Photo> = ArrayList()
        withContext(Dispatchers.IO) {
            try {
                val myLink = StringBuilder()
                myLink.append(BASE_URL).append("page-$page.html")
                val doc = Jsoup.connect(myLink.toString()).get()
                val links = doc.select("div.loop")

                for (element in links) {
//                    val link = element.select("a[href]").attr("href")
                    val thumb = element.select("img[src$=.jpg]").attr("src")
                    val res = URI(thumb).query.substring(4) // "从src开始"
                    val title = res.substringAfterLast('/')
                    val photo = Photo(res, title)
                    resultPhoto.add(photo)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return resultPhoto
    }
}
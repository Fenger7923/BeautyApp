package com.laosiji.beautyapp.network

import android.net.Uri
import com.laosiji.beautyapp.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URI

/**
 * @author fengerzhang
 * @date 2021/11/30 17:01
 */
object HttpController {
    private const val BASE_URL = "http://www.xiuren.org/"

    suspend fun getPhotoFormServer(page: Int) : List<Photo> {
        val resultPhoto: MutableList<Photo> = ArrayList()
        withContext(Dispatchers.IO) {
            try {
                val myLink = StringBuilder()
                myLink.append(BASE_URL).append("page-$page.html")
                val doc = Jsoup.connect(myLink.toString()).get()
                val links = doc.select("div.loop")

                for (element in links) {
                    val title = element.select("a").attr("title")
                    val thumb = element.select("img[src$=.jpg]").attr("src")
                    val res = Uri.parse(thumb).getQueryParameter("src") ?: thumb
                    val link = element.select("a[href]").attr("href")

                    val photo = Photo(link, res, title)
                    resultPhoto.add(photo)
                }
            } catch (e: IOException) {
                e.printStackTrace()
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
                    val link = element.select("a[href]").attr("href")
                    val thumb = element.select("img[src$=.jpg]").attr("src")
                    val res = URI(thumb).query.substring(4) // "从src开始"
                    val title = res.substringAfterLast('/')
                    val photo = Photo(link, res, title)
                    resultPhoto.add(photo)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return resultPhoto
    }
}
package com.laosiji.beautyapp.pachong

import com.laosiji.beautyapp.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URI
import java.net.URL
import java.net.URLConnection

/**
 * @author fengerzhang
 * @date 2021/12/8 20:50
 */
object NetAndFileOperator {
    private const val FAILED_FILE_NAME = "error_pic.txt"
    private const val SUCCESS_FILE_NAME = "picList.txt"
    private const val XIU_REN_URL = "http://www.xiuren.org/"
    private const val MS_GAO_URL = "https://www.msgao.com/meinv/"

    suspend fun getXiuRenServerPic(page: Int): List<Photo> {
        val resultPhoto: MutableList<Photo> = ArrayList()
        withContext(Dispatchers.IO) {
            val myLink = StringBuilder()
            myLink.append(XIU_REN_URL).append("page-$page.html")
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
        }
        return resultPhoto
    }

    fun downLoadPicture(link: String, title: String) {
        // 成功则存储
        try {
            val url1 = URL(link)
            val uc: URLConnection = url1.openConnection()
            val inputStream: InputStream = uc.getInputStream()
            val sf = File("download")
            if (!sf.exists()) {
                sf.mkdirs()
            }
            val out = FileOutputStream(sf.path + "/" + title)
            var j: Int
            while (inputStream.read().also { j = it } != -1) {
                out.write(j)
            }
            inputStream.close()
            writeToFile(title, SUCCESS_FILE_NAME)
        } catch (e: Exception) {
            // 失败则丢到txt里面
            writeToFile(link)
        }
    }

    @Synchronized
    fun writeToFile(link: String, fileName: String = FAILED_FILE_NAME) {
        val out = BufferedWriter(FileWriter(fileName, true))
        out.write(link)
        out.newLine()
        out.close()
    }

    fun readFromNativeFile(fileName: String): List<String> {
        val result: MutableList<String> = mutableListOf()


        val `in` = BufferedReader(FileReader(fileName))
        var item: String?
        while (`in`.readLine().also { item = it } != null) {
            item?.let { result.add(it) }
        }
        return result
    }
}


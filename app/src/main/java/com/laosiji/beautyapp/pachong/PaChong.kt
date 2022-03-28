package com.laosiji.beautyapp.pachong

import com.laosiji.beautyapp.Personal
import com.laosiji.beautyapp.Photo
import com.laosiji.beautyapp.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

const val TYPE_TXT = "typeList.txt"
const val PERSONAL_TXT = "personalList.txt"
const val PHOTO_TXT = "photoList.txt"
suspend fun main() {
//    getTypeListTxt()

//    getPersonalListTxt()

//    getPhotoListTxt()
}

object Net111 {
    private const val MS_GAO_URL = "https://www.msgao.com"

    suspend fun getHomePageData(): List<Type> {
        val resultType: MutableList<Type> = mutableListOf()
        withContext(Dispatchers.IO) {
            try {

                val myLink = StringBuilder()
                myLink.append("$MS_GAO_URL/meinv/")
                val doc = Jsoup.connect(myLink.toString()).get()

                val typeListHtml = doc.select("div.warp.mndh").select("a[href]")// 分类列表
                typeListHtml.forEach { type ->
                    resultType.add(Type(type.attr("href").substringAfter('/'), type.ownText()))
                }
            } catch (e: IOException) {

            }
        }
        return resultType
    }

    // page 都是从1开始, 默认的type是 /meinv/
    suspend fun getPersonalData(type: Type, page: Int = 1): List<Personal> {
        val resultPersonal: MutableList<Personal> = mutableListOf()
        withContext(Dispatchers.IO) {
            val myLink = StringBuilder()
            myLink.append("$MS_GAO_URL/${type.link}/index${if (page != 1) "_$page" else ""}.html")
            val doc = try {
                Jsoup.connect(myLink.toString()).get()
            } catch (e: Exception) {
                return@withContext
            }

            val picListHtml = doc.select("div.listmainrows.left") // 美图
            val pageListHtml = doc
                .select("div.page.both")
                .select("a[href]")
                .last()
                .attr("href")
            // 最大页码
            val maxPage = getNumbers(pageListHtml)
            //筛选是可以点击进去详情的url
            picListHtml.forEach {
                val realUrl = it.select("a[href]")
                //不正确的全部移除
                if (realUrl.size == 0) {
                    picListHtml.remove(it)
                }
            }

            picListHtml.forEach {
                val realUrl = it.select("a[href]")
                val link = realUrl[0].attr("href").substringBefore(".html")
                val title = realUrl[0].attr("title")
                //获取图片地址
                val imgSrcs = it.select("img[src\$=.jpg]").attr("src")
                //添加bean到集合中
                resultPersonal.add(Personal(link, title, imgSrcs, maxPage))
            }
        }
        return resultPersonal.distinctBy { it.link }
    }

    // 对应的personal，拿到所有的图片
    suspend fun getPhotoData(personal: Personal): List<Photo> {
        val resultPhoto: MutableList<Photo> = mutableListOf()
        withContext(Dispatchers.IO) {
            try {
                val myLink = StringBuilder()
                // 先拿到第一页数据
                myLink.append("$MS_GAO_URL${personal.link}.html")
                val doc = try {
                    Jsoup.connect(myLink.toString()).get()
                } catch (e: Exception) {
                    return@withContext
                }
                val pageListHtml = doc
                    .select("div.page")
                    .select("a[href]")
                    .last()
                    .attr("href")
                // 最大页码
                val maxPage = getNumbers(pageListHtml)

                val picHtml = doc.select("div.bg-white.p15.center.imgac.clearfix")
                picHtml.forEach {
                    val image = it.select("img[src\$=.jpg]")
                    resultPhoto.add(Photo(image.attr("src"), image.attr("alt")))
                }

                for (index in 2..maxPage) {
                    val pageLink = StringBuilder()
                    pageLink.append("$MS_GAO_URL${personal.link}_$index.html")
                    val docInPage = Jsoup.connect(pageLink.toString()).get()
                    val picHtmlInPage = docInPage.select("div.bg-white.p15.center.imgac.clearfix")
                    picHtmlInPage.forEach {
                        val image = it.select("img[src\$=.jpg]")
                        resultPhoto.add(Photo(image.attr("src"), image.attr("alt")))
                    }
                }
            } catch (e: Exception) {
                println("err in just one request")
            }
        }
        return resultPhoto
    }

    //截取数字
    private fun getNumbers(content: String): Int {
        return try {
            content.substringAfter('_').substringBefore(".html").toInt()
        } catch (e: Exception) {
            0
        }
    }

    fun splitList(
        messagesList: List<String>,
        groupSize: Int
    ): List<List<String>> {
        val length = messagesList.size
        // 计算可以分成多少组
        val num = (length + groupSize - 1) / groupSize // TODO
        val newList: MutableList<List<String>> = ArrayList(num)
        for (i in 0 until num) {
            // 开始位置
            val fromIndex = i * groupSize
            // 结束位置
            val toIndex = if ((i + 1) * groupSize < length) (i + 1) * groupSize else length
            newList.add(messagesList.subList(fromIndex, toIndex))
        }
        return newList
    }
}
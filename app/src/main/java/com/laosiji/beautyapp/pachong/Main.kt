package com.laosiji.beautyapp.pachong

import com.laosiji.beautyapp.Photo
import com.laosiji.beautyapp.network.HttpController
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

/**
 * @author fengerzhang
 * @date 2021/12/8 15:59
 */
const val FAILED_FILE_NAME = "error_pic.txt"
const val SUCCESS_FILE_NAME = "picList.txt"

suspend fun main() {
    startToPullUrl()
}

suspend fun startToPullUrl() {
    val endList: MutableList<Photo> = mutableListOf()
    for (index in 1..100) {
        val photoList = HttpController.getPhotoFormServerInJava(index)
        endList.addAll(photoList)
    }
    endList.forEach {
        downLoad(it.thumb, it.title)
    }
}

fun writeToFile(link: String, fileName: String = FAILED_FILE_NAME) {
    val out = BufferedWriter(FileWriter(fileName, true))
    out.write(link)
    out.newLine()
    out.close()
}

fun downLoad(link: String, title: String) {
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
        var j = 0
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
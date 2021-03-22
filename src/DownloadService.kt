package com.example

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import kotlin.math.roundToInt

@KtorExperimentalAPI
class DownloadService {

    @ExperimentalIoApi
    fun downloadFile(url: String): Flow<DownloadStatus> = flow {

        val httpClient = HttpClient()
        val statement = httpClient.request<HttpStatement>(url)

        statement.execute {
            val byteArray = ByteArray(it.contentLength()?.lowInt ?:0)
            val readChannel = it.content
            var offset = 0

            runCatching {
                do {
                    val currentRead = readChannel.readAvailable(byteArray, offset, byteArray.size)
                    offset += currentRead
                    val progress = (offset * 100f / byteArray.size).roundToInt()
                    emit(DownloadStatus.Progress(progress))
                } while (currentRead > 0)

                val file = File("${System.getProperty("user.home")}/${url.fileName()}")
                file.writeBytes(byteArray)
            }.onFailure { throwable ->
                //TODO:exceotion 세분화하기
                emit(DownloadStatus.Error("File Download Error : ${throwable.localizedMessage}"))
            }.onSuccess {
                emit(DownloadStatus.Success)
            }
        }
    }
}

fun String.fileName(): String {
    return this.substringAfterLast("/")
}

sealed class DownloadStatus {

    object Success : DownloadStatus()

    data class Error(val message: String) : DownloadStatus()

    data class Progress(val progress: Int): DownloadStatus()

}
package com.example

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.collect

@ExperimentalIoApi
@KtorExperimentalAPI
fun Routing.download(service: DownloadService) {

    route("file") {
        get("/download") {
            call.request.queryParameters["url"]?.let { it1 ->
                service.downloadFile(it1)
                    .collect {
                        when(it) {
                            is DownloadStatus.Success -> {
                                println("Success")
                            }
                            is DownloadStatus.Error -> {
                                println(it)
                            }
                            is DownloadStatus.Progress -> {
                                println("downloading => ${it.progress}%")
                            }
                        }
                    }
            } ?: kotlin.run {
                val msg = "download url is empty"
                println(msg)
                call.respondText(msg)
            }
        }
    }
}
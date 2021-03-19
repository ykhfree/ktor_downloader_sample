package com.example

import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.collect

@ExperimentalIoApi
@KtorExperimentalAPI
fun Routing.download(service: DownloadService) {

    route("file") {
        get("/download") {
            service.downloadFile()
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
        }
    }
}
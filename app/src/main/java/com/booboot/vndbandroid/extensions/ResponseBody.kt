package com.booboot.vndbandroid.extensions

import okhttp3.ResponseBody
import okio.BufferedSource
import okio.Okio
import java.io.File

fun ResponseBody.saveToDisk(filename: String, directory: File): File =
    File.createTempFile(filename, null, directory).apply {
        source().write(this)
    }

fun BufferedSource.write(file: File) = Okio.buffer(Okio.sink(file)).let {
    it.writeAll(this)
    it.close()
}
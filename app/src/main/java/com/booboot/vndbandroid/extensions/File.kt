package com.booboot.vndbandroid.extensions

import okio.BufferedSource
import okio.Okio
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.GZIPInputStream


fun File.unzip(): File = try {
    val input = GZIPInputStream(BufferedInputStream(FileInputStream(this)))
    val outputFile = File(parent, name + System.currentTimeMillis())
    val output = BufferedOutputStream(FileOutputStream(outputFile))
    val buffer = ByteArray(1024 * 4)

    while (true) {
        val count = input.read(buffer)
        if (count < 0) break
        output.write(buffer, 0, count)
    }

    output.flush()
    output.close()
    input.close()
    outputFile
} finally {
    delete()
}

fun <T> File.use(action: (File) -> T): T = try {
    action(this)
} finally {
    delete()
}

fun File.toBufferedSource(): BufferedSource = Okio.buffer(Okio.source(this))
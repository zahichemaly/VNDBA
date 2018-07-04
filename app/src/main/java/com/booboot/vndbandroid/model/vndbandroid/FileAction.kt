package com.booboot.vndbandroid.model.vndbandroid

import java.io.File

/**
 * Temporary class used with Rx to zip a VN and Tags in a single object.
 */
data class FileAction(
    val file: File,
    val action: Int
)
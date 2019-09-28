package com.booboot.vndbandroid.extensions

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

/**
 * Saves a Bitmap to the Images Media Store (Shared Collection, according to the new Android 10 policy).
 * @param context Application context
 * @param filename The name of the file as displayed to the user
 * @return The content URI in which the file was saved (starting with content://). This URI cannot be used with File nor MediaScannerConnection.
 */
@Throws
fun Bitmap.saveToMediaStore(context: Context, filename: String): Uri {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, filename)
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DATE_ADDED, currentTimeSeconds())
        put(MediaStore.Images.Media.DATE_MODIFIED, currentTimeSeconds())
    }
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri ?: throw Exception("The image cannot be saved.")
    val fileDescriptor = contentResolver.openFileDescriptor(uri, "rwt")
    val fileOutputStream = ParcelFileDescriptor.AutoCloseOutputStream(fileDescriptor)
    val result = compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    if (!result) throw Exception("Error while compressing the image.")
    fileOutputStream.flush()
    fileOutputStream.close()
    fileDescriptor?.close()

    contentResolver.openInputStream(uri)?.let { istream ->
        contentResolver.update(uri, ContentValues().apply {
            put(MediaStore.Images.Media.SIZE, istream.available())
        }, null, null)
    }

    return uri
}

/**
 * Saves a Bitmap to a directory identified by a File.
 * @param context Application context
 * @param filename The name of the file as displayed to the user
 * @param directory The directory to which the file must be saved
 * @return The content URI in which the file was saved (starting with content://). This URI cannot be used with File nor MediaScannerConnection.
 */
@Throws
fun Bitmap.saveToDirectory(context: Context, filename: String, directory: File): Uri {
    val file = File(directory, filename)
    file.createNewFile()
    val ostream = FileOutputStream(file)
    compress(Bitmap.CompressFormat.JPEG, 100, ostream)
    ostream.close()
    return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
}
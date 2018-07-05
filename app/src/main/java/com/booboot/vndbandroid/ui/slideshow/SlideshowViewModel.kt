package com.booboot.vndbandroid.ui.slideshow

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.model.vndbandroid.FileAction
import com.booboot.vndbandroid.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

class SlideshowViewModel constructor(application: Application) : BaseViewModel(application) {
    val fileData: MutableLiveData<FileAction> = MutableLiveData()

    fun downloadScreenshot(bitmap: Bitmap?, action: Int, directory: File) {
        if (disposables.contains(DISPOSABLE_DOWNLOAD_SCREENSHOT)) return

        disposables[DISPOSABLE_DOWNLOAD_SCREENSHOT] = Single.fromCallable {
            if (bitmap == null) throw Exception("The image is not ready yet.")

            val filename = String.format("IMAGE_%d.jpg", System.currentTimeMillis())
            val file = File(directory, filename)

            file.createNewFile()
            val ostream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
            ostream.close()
            file
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.value = true }
            .doFinally {
                loadingData.value = false
                disposables.remove(DISPOSABLE_DOWNLOAD_SCREENSHOT)
            }
            .subscribe({
                fileData.value = FileAction(it, bitmap!!, action)
                fileData.value = null
            }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_DOWNLOAD_SCREENSHOT = "DISPOSABLE_DOWNLOAD_SCREENSHOT"
    }
}
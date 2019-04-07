package com.booboot.vndbandroid.ui.slideshow

import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.minus
import com.booboot.vndbandroid.extensions.plus
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.FileAction
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SlideshowViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository

    val vnData: MutableLiveData<VN> = MutableLiveData()
    val fileData: MutableLiveData<FileAction> = MutableLiveData()
    var position: Int = -1
    var vnId: Long = -1

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(force: Boolean = true) {
        if (!force && vnData.value != null) return
        if (disposables.contains(DISPOSABLE_VN)) return

        disposables[DISPOSABLE_VN] = vnRepository.getItem(vnId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.plus() }
            .doFinally {
                loadingData.minus()
                disposables.remove(DISPOSABLE_VN)
            }
            .subscribe({ vnData.value = it }, ::onError)
    }

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
            .doOnSubscribe { loadingData.plus() }
            .doFinally {
                loadingData.minus()
                disposables.remove(DISPOSABLE_DOWNLOAD_SCREENSHOT)
            }
            .subscribe({ fileData.value = FileAction(it, bitmap!!, action) }, ::onError)
    }

    override fun restoreState(state: Bundle?) {
        super.restoreState(state)
        state ?: return
        position = state.getInt(POSITION)
    }

    override fun saveState(state: Bundle) {
        super.saveState(state)
        state.putInt(POSITION, position)
    }

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
        private const val DISPOSABLE_DOWNLOAD_SCREENSHOT = "DISPOSABLE_DOWNLOAD_SCREENSHOT"
        private const val POSITION = "POSITION"
    }
}
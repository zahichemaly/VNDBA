package com.booboot.vndbandroid.ui.vnrelations

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RelationsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    val vnData: MutableLiveData<VNWithRelations> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(vnId: Long, force: Boolean = true) {
        if (!force && vnData.value != null) return
        if (disposables.contains(DISPOSABLE_VN)) return

        var vn = VN()
        disposables[DISPOSABLE_VN] = vnRepository.getItem(vnId)
            .subscribeOn(Schedulers.newThread())
            .doOnSubscribe { loadingData.value = true }
            .observeOn(Schedulers.newThread())
            .flatMap {
                vn = it
                vnRepository.getItems(it.relations.mapTo(hashSetOf()) { it.id }, FLAGS_DETAILS)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loadingData.value = false
                disposables.remove(DISPOSABLE_VN)
            }
            .subscribe({ vnData.value = VNWithRelations(vn, it) }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
    }
}

data class VNWithRelations(
    val vn: VN = VN(),
    val relations: Map<Long, VN> = emptyMap()
)
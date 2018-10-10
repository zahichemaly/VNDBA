package com.booboot.vndbandroid.ui.vnrelations

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.minus
import com.booboot.vndbandroid.extensions.plus
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RelationsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    val vnData: MutableLiveData<RelationsData> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(vnId: Long, force: Boolean = true) {
        if (!force && vnData.value != null) return
        if (disposables.contains(DISPOSABLE_VN)) return

        var items = AccountItems()
        disposables[DISPOSABLE_VN] = accountRepository.getItems()
            .subscribeOn(Schedulers.newThread())
            .doOnSubscribe { loadingData.plus() }
            .observeOn(Schedulers.newThread())
            .flatMap {
                items = it
                val vn = it.vns[vnId] ?: throw Throwable("VN not found.")
                vnRepository.getItems(vn.relations.mapTo(hashSetOf()) { it.id }, FLAGS_DETAILS)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loadingData.minus()
                disposables.remove(DISPOSABLE_VN)
            }
            .subscribe({ vnData.value = RelationsData(vnId, items, it) }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
    }
}

data class RelationsData(
    val vnId: Long = 0,
    val items: AccountItems = AccountItems(),
    val relations: Map<Long, VN> = emptyMap()
)
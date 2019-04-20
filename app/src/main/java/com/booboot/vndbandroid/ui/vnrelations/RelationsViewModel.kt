package com.booboot.vndbandroid.ui.vnrelations

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.diff.RelationsDiffCallback
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RelationsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    val relationsData: MutableLiveData<RelationsData> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(vnId: Long, oldRelationsData: RelationsData, force: Boolean = true) = coroutine(DISPOSABLE_VN, !force && relationsData.value != null) {
        val accountItemsJob = accountRepository.getItems(this)
        val vnJob = vnRepository.getItem(this, vnId)
        val accountItems = accountItemsJob.await()
        val vn = vnJob.await()
        relationsData.value = withContext(Dispatchers.Default) {
            val accountItemsCopy = accountItems.deepCopy()
            val relations = vnRepository.getItems(this, vn.relations.mapTo(hashSetOf()) { it.id }, FLAGS_DETAILS).await()
            RelationsData(vn, accountItemsCopy, relations).apply {
                val diffCallback = RelationsDiffCallback(oldRelationsData, this)
                diffResult = DiffUtil.calculateDiff(diffCallback)
            }
        }
    }

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
    }
}

data class RelationsData(
    val vn: VN = VN(),
    val items: AccountItems = AccountItems(),
    val relations: Map<Long, VN> = emptyMap(),
    var diffResult: DiffUtil.DiffResult? = null
)
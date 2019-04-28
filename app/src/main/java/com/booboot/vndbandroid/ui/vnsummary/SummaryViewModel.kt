package com.booboot.vndbandroid.ui.vnsummary

import android.app.Application
import android.text.SpannableString
import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.format
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SummaryViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    val summaryData: MutableLiveData<SummaryVN> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(vnId: Long, force: Boolean = true) = coroutine(DISPOSABLE_VN, !force && summaryData.value != null) {
        val vn = vnRepository.getItem(this, vnId).await()
        summaryData.value = withContext(Dispatchers.IO) {
            vn.aliases = vn.aliases?.replace("\n", ", ")
            val description = vn.description?.format(getApplication<Application>().packageName) ?: SpannableString("")
            SummaryVN(vn, description)
        }
    }

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
    }
}

data class SummaryVN(
    val vn: VN,
    val description: Spanned
)
package com.booboot.vndbandroid.ui.vntags

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Genre
import com.booboot.vndbandroid.model.vndbandroid.VNAndTags
import com.booboot.vndbandroid.model.vndbandroid.VNDetailsTags
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TagsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var tagsRepository: TagsRepository
    val tagsData: MutableLiveData<VNDetailsTags> = MutableLiveData()
    private val fullTagsData: MutableLiveData<VNDetailsTags> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadTags(vnId: Int, force: Boolean = true) {
        if (!force && tagsData.value != null) return
        if (disposables.contains(DISPOSABLE_LOAD_TAGS)) return

        val vnObservable = vnRepository.getItem(vnId).subscribeOn(Schedulers.newThread())
        val tagsObservable = tagsRepository.getItems().subscribeOn(Schedulers.newThread())

        disposables[DISPOSABLE_LOAD_TAGS] = Single.zip(vnObservable, tagsObservable, BiFunction<VN, Map<Int, Tag>, VNAndTags> { vn, tags ->
            VNAndTags(vn, tags)
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.value = true }
            .observeOn(Schedulers.computation())
            .map { vnAndTags ->
                val sortedTags = vnAndTags.vn.tags.sortedByDescending { it[1].toInt() }.mapNotNull { tagInfo ->
                    vnAndTags.tags[tagInfo[0]]?.let { tag -> VNTag(tag, tagInfo) }
                }

                val categories = mutableMapOf(
                    Tag.KEY_GENRES to sortedTags.filter { Genre.contains(it.tag.name) },
                    Tag.KEY_POPULAR to sortedTags.take(10)
                )
                categories.putAll(sortedTags.groupBy { it.tag.cat })
                VNDetailsTags(categories.toSortedMap(compareBy { Tag.CATEGORIES.keys.indexOf(it) }))
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loadingData.value = false
                disposables.remove(DISPOSABLE_LOAD_TAGS)
            }
            .subscribe({
                tagsData.value = it
                fullTagsData.value = it
            }, ::onError)
    }

    fun collapseTags(title: String) {
        val tags = tagsData.value?.copy() ?: return
        tags.all[title] = tags.all[title]?.let {
            if (it.isNotEmpty()) emptyList()
            else fullTagsData.value?.all?.get(title) ?: emptyList()
        } ?: emptyList()
        tagsData.value = tags
    }

    companion object {
        private const val DISPOSABLE_LOAD_TAGS = "DISPOSABLE_LOAD_TAGS"
    }
}
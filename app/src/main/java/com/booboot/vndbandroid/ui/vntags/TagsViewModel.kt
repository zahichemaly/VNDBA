package com.booboot.vndbandroid.ui.vntags

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndbandroid.Genre
import com.booboot.vndbandroid.model.vndbandroid.VNDetailsTags
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

class TagsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var tagsRepository: TagsRepository
    val tagsData: MutableLiveData<VNDetailsTags> = MutableLiveData()
    private val fullTagsData: MutableLiveData<VNDetailsTags> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadTags(vnId: Long, force: Boolean = true) = coroutine(DISPOSABLE_LOAD_TAGS, !force && tagsData.value != null) {
        val vnJob = async { vnRepository.getItem(vnId) }
        val tagsJob = async { tagsRepository.getItems() }
        val vn = vnJob.await()
        val tags = tagsJob.await()
        val sortedTags = vn.tags.asSequence().sortedByDescending { it[1] }.mapNotNull { tagInfo ->
            tags[tagInfo[0].toLong()]?.let { tag -> VNTag(tag, tagInfo) }
        }.toList()

        val categories = mutableMapOf(
            Tag.KEY_GENRES to sortedTags.filter { Genre.contains(it.tag.name) },
            Tag.KEY_POPULAR to sortedTags.take(10)
        )
        categories.putAll(sortedTags.groupBy { it.tag.cat })
        val vnDetailsTags = VNDetailsTags(categories.filterValues { it.isNotEmpty() }.toSortedMap(compareBy { Tag.CATEGORIES.keys.indexOf(it) }))
        tagsData += vnDetailsTags
        fullTagsData += vnDetailsTags
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
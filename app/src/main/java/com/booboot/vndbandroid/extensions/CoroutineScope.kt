package com.booboot.vndbandroid.extensions

import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Label
import com.booboot.vndbandroid.model.vndb.UserLabel
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.ui.filters.FilterSubtitleItem
import com.booboot.vndbandroid.ui.filters.LabelItem
import com.booboot.vndbandroid.ui.filters.VoteItem
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

fun <T> CoroutineScope.asyncLazy(block: suspend CoroutineScope.() -> T) = async(Dispatchers.Unconfined, CoroutineStart.LAZY, block)

/**
 * Kotlin decided for some reason that CoroutineExceptionHandler didn't work for async blocks (see https://kotlinlang.org/docs/reference/coroutines/exception-handling.html#coroutineexceptionhandler).
 * So in order to execute an exception handler for an async block AND also its parent coroutine's exception handler, here's an extension
 * which catches any error thrown, executes a custom exception handler, and throw it back for its parent's exception handler.
 *
 * @param onError the exception handler = block to be executed in case of error.
 * @param block the usual async body with the work to be done asynchronously.
 */
fun <T> CoroutineScope.asyncWithError(onError: (Throwable) -> Unit, block: suspend CoroutineScope.() -> T) = async(Dispatchers.IO) {
    try {
        block()
    } catch (t: Throwable) {
        onError(t)
        throw t
    }
}

suspend fun <T> catchAndRethrow(onError: (Throwable) -> Unit, block: suspend () -> T) = try {
    block()
} catch (t: Throwable) {
    onError(t)
    throw t
}

fun CoroutineScope.categorizeLabels(
    accountItems: AccountItems,
    onLabelClicked: (UserLabel) -> Unit,
    onVoteClicked: (UserLabel) -> Unit,
    isLabelSelected: (Long) -> Boolean,
    isVoteSelected: (Long) -> Boolean,
    addCustomVotes: (LinkedHashMap<FilterSubtitleItem, MutableList<Item>>, FilterSubtitleItem, UserLabel) -> Unit
) = async {
    val categorizedLabels = linkedMapOf<FilterSubtitleItem, MutableList<Item>>()
    accountItems.userLabels.values.forEach { userLabel ->
        val selected = isLabelSelected(userLabel.id)
        when (userLabel.id) {
            in Label.STATUSES -> categorizedLabels.addOrCreate(FilterSubtitleItem(R.drawable.ic_list_48dp, R.string.status), LabelItem(userLabel, selected, onLabelClicked))
            in Label.WISHLISTS -> categorizedLabels.addOrCreate(FilterSubtitleItem(R.drawable.ic_wishlist, R.string.wishlist), LabelItem(userLabel, selected, onLabelClicked))
            Label.VOTED.id -> {
                val subtitleItem = FilterSubtitleItem(R.drawable.ic_format_list_numbered_48dp, R.string.votes)
                /* Votes from 1 to 10 */
                for (vote in Label.VOTES) categorizedLabels.addOrCreate(subtitleItem, VoteItem(UserLabel(vote.id, vote.label), isVoteSelected(vote.id), onVoteClicked))
                addCustomVotes(categorizedLabels, subtitleItem, userLabel)
            }
            else -> categorizedLabels.addOrCreate(FilterSubtitleItem(R.drawable.ic_list_48dp, R.string.category_custom_labels), LabelItem(userLabel, selected, onLabelClicked))
        }
    }
    categorizedLabels
}
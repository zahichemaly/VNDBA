package com.booboot.vndbandroid.ui.logout

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.di.BoxManager
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.TraitsRepository
import com.booboot.vndbandroid.repository.UserLabelsRepository
import com.booboot.vndbandroid.repository.UserListRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.chibatching.kotpref.bulk
import javax.inject.Inject

class LogoutViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var boxManager: BoxManager
    @Inject lateinit var tagsRepository: TagsRepository
    @Inject lateinit var traitsRepository: TraitsRepository
    @Inject lateinit var userLabelsRepository: UserLabelsRepository
    @Inject lateinit var userListRepository: UserListRepository
    @Inject lateinit var vnRepository: VNRepository

    val logoutData = MutableLiveData<Boolean>()

    init {
        (application as App).appComponent.inject(this)
    }

    fun logout() = coroutine(JOB_LOGOUT) {
        Preferences.bulk {
            password = null
            loggedIn = false
        }

        tagsRepository.clear()
        traitsRepository.clear()
        userLabelsRepository.clear()
        userListRepository.clear()
        vnRepository.clear()

        boxManager.reset()
        VNDBServer.closeAll()
        getApplication<App>().cacheDir.deleteRecursively()

        logoutData.postValue(true)
    }

    companion object {
        private const val JOB_LOGOUT = "JOB_LOGOUT"
    }
}
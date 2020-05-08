package com.luiz.reminder.ui.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luiz.reminder.api.services.ApiRepository
import com.luiz.reminder.di.DaggerViewModelComponent
import com.luiz.reminder.util.Utils
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    lateinit var context: Context

    val loadError by lazy { MutableLiveData<String>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    val disposable = CompositeDisposable()

    open fun beforeRequest() {
        loading.postValue(true)
    }

    open fun afterRequest() {
        loading.postValue(false)
    }

    open fun afterRequest(e: Throwable?) {
        loading.postValue(false)
        loadError.postValue(Utils.getMessageErrorObject(e!!))
    }

    @Inject
    protected lateinit var apiRepository: ApiRepository

    init {
        DaggerViewModelComponent.create().inject(this)
    }
}
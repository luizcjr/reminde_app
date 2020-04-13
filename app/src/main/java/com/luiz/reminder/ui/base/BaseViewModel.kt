package com.luiz.reminder.ui.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luiz.reminder.api.services.ApiRepository
import com.luiz.reminder.di.DaggerViewModelComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    lateinit var context: Context

    val loadError by lazy { MutableLiveData<String>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    val disposable = CompositeDisposable()

    @Inject
    protected lateinit var apiRepository: ApiRepository

    init {
        DaggerViewModelComponent.create().inject(this)
    }
}
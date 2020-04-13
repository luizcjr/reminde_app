package com.luiz.reminder.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.luiz.reminder.util.Utils

abstract class BaseActivity : AppCompatActivity() {

    protected val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        if (isLoading) {
            Utils.onStartLoading(this)
        } else {
            Utils.onStopLoading()
        }
    }

    protected val errorLiveDataObserver = Observer<String> { error ->
        if (error != null) {
            Utils.alertError(error, this)
        }
    }

}
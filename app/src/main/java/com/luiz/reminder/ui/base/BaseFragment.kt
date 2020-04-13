package com.luiz.reminder.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.luiz.reminder.api.models.User
import com.luiz.reminder.ui.activities.main.MainActivity
import com.luiz.reminder.util.Utils

abstract class BaseFragment : Fragment() {

    protected val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        if (isLoading) {
            Utils.onStartLoading(context!!)
        } else {
            Utils.onStopLoading()
        }
    }

    protected val errorLiveDataObserver = Observer<String> { error ->
        if (error != null) {
            Utils.alertError(error, context!!)
        }
    }

    val userLiveDataObserver = Observer<User> { user ->
        user.let {
            getParentActivity()!!.title = user.name
        }
    }

    protected fun getParentActivity(): MainActivity? {
        return activity as MainActivity?
    }
}
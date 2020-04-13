package com.luiz.reminder.ui.fragments.reminde

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.luiz.reminder.R
import com.luiz.reminder.api.models.Notes
import com.luiz.reminder.api.models.User
import com.luiz.reminder.api.responses.NotesResponse
import com.luiz.reminder.api.responses.UserResponse
import com.luiz.reminder.ui.activities.include_reminde.IncludeRemindeActivity
import com.luiz.reminder.ui.activities.login.LoginActivity
import com.luiz.reminder.ui.base.BaseViewModel
import com.luiz.reminder.util.Utils
import com.luiz.reminder.util.Utils.openActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class RemindeViewModel : BaseViewModel() {

    val user by lazy { MutableLiveData<User>() }
    val notes by lazy { MutableLiveData<List<Notes>>() }

    fun getAllNotes() {
        loading.value = true

        disposable.add(
            apiRepository.getAllNotes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NotesResponse>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loadError.value = Utils.getMessageErrorObject(e)
                        loading.value = false
                        notes.value = null
                    }

                    override fun onSuccess(t: NotesResponse) {
                        loadError.value = null
                        loading.value = false

                        notes.value = t.notes
                        Log.d("_res", "Notes view: " + Gson().toJson(t.notes))
                    }
                })
        )
    }

    fun getUserInfo() {
        loading.value = true

        disposable.add(
            apiRepository.getUserInfo(Utils.getLastUserSession()!!.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserResponse>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loadError.value = Utils.getMessageErrorObject(e)
                        loading.value = false
                        user.value = null
                    }

                    override fun onSuccess(t: UserResponse) {
                        loadError.value = null
                        loading.value = false

                        user.value = t.user
                    }
                })
        )
    }

    fun logout() {
        Utils.setApiToken(null)
        Utils.setLastUserSession(null)
        redirectToLogin()
    }

    fun redirectToIncludeReminder() {
        context.openActivity<IncludeRemindeActivity>()
    }

    private fun redirectToLogin() {
        context.openActivity<LoginActivity>(
            enterAnim = R.anim.slide_from_right,
            exitAnim = R.anim.slide_to_left
        ) {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
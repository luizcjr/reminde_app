package com.luiz.reminder.ui.activities.splash_screen

import android.content.Intent
import com.google.gson.JsonObject
import com.luiz.reminder.api.responses.FCMResponse
import com.luiz.reminder.api.responses.LoginResponse
import com.luiz.reminder.ui.activities.login.LoginActivity
import com.luiz.reminder.ui.activities.main.MainActivity
import com.luiz.reminder.ui.base.BaseViewModel
import com.luiz.reminder.util.Utils
import com.luiz.reminder.util.Utils.openActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody

class SplashScreenViewModel : BaseViewModel() {

    private fun fcmTokenBody(): RequestBody {
        loading.value = true

        val json = JsonObject()
        json.addProperty("fcm_token", Utils.getApiFCMToken())
        json.addProperty("is_accepted", true)

        return RequestBody.create(MediaType.parse("application/json"), json.toString())
    }

    fun sendFcmToken() {
        disposable.add(
            apiRepository.sendFCMToken(fcmTokenBody())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<FCMResponse>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loadError.value = Utils.getMessageErrorObject(e)
                        loading.value = false
                    }

                    override fun onSuccess(t: FCMResponse) {
                        loadError.value = null
                        loading.value = false
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
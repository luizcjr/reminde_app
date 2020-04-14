package com.luiz.reminder.ui.activities.main

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.luiz.reminder.api.models.User
import com.luiz.reminder.api.responses.FCMResponse
import com.luiz.reminder.ui.base.BaseViewModel
import com.luiz.reminder.util.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody


class MainViewModel : BaseViewModel() {

    private fun fcmTokenBody(fcmToken: String): RequestBody {
        loading.value = true

        val json = JsonObject()
        json.addProperty("fcm_token", fcmToken)
        json.addProperty("is_accepted", true)

        Log.d("_res", "Json: ${Gson().toJson(json)}")
        return RequestBody.create(MediaType.parse("application/json"), json.toString())
    }

    fun verifyHasToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task: Task<InstanceIdResult> ->
                if (!task.isSuccessful) {
                    Log.d("_res", "Is not successful")
                    Toast.makeText(
                        context,
                        "Erro ao obter token do firebase para esse dispositivo",
                        Toast.LENGTH_LONG
                    ).show()
                    return@addOnCompleteListener
                }

                // First access (F-Register FCM Token)
                if (Utils.getApiFCMToken() == null) {
                    Log.d("_res", "First access")
                    sendFcmToken(fcmTokenBody(task.result!!.token))
                } else {
                    // Get new Instance ID token
                    if (!Utils.getApiFCMToken()
                            .equals((task.result)!!.token)
                    ) {
                        Log.d("_res", "Get new Instance ID token")
                        sendFcmToken(fcmTokenBody(task.result!!.token))
                    } else {
                        sendFcmToken(fcmTokenBody(Utils.getApiFCMToken()!!))
                    }
                }
            }
    }

    private fun sendFcmToken(body: RequestBody) {
        disposable.add(
            apiRepository.sendFCMToken(body)
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
package com.luiz.reminder.ui.fragments.profile

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.luiz.reminder.R
import com.luiz.reminder.api.models.User
import com.luiz.reminder.api.responses.UserResponse
import com.luiz.reminder.ui.activities.login.LoginActivity
import com.luiz.reminder.ui.base.BaseViewModel
import com.luiz.reminder.util.Utils
import com.luiz.reminder.util.Utils.openActivity
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody

class ProfileViewModel : BaseViewModel() {

    val user by lazy { MutableLiveData<User>() }
    val name = ValiFieldText().addNotEmptyValidator("Campo obrigatório!")
    val email = ValiFieldEmail().addEmailValidator("E-mail inválido!")
    val form = ValiFiForm(email, name)

    private fun updateUserBody(): RequestBody {
        loading.value = true

        val json = JsonObject()
        json.addProperty("name", name.value)
        json.addProperty("email", email.value)

        return RequestBody.create(MediaType.parse("application/json"), json.toString())
    }

    fun updateUser() {
        disposable.add(
            apiRepository.updateUser(updateUserBody(), Utils.getLastUserSession()!!.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserResponse>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loadError.value = Utils.getMessageErrorObject(e)
                        loading.value = false
                    }

                    override fun onSuccess(t: UserResponse) {
                        loadError.value = null
                        loading.value = false

                        user.value = t.user
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
                    }

                    override fun onSuccess(t: UserResponse) {
                        loadError.value = null
                        loading.value = false

                        user.value = t.user
                        Log.d("_res", "User profile: " + Gson().toJson(t.user))
                    }
                })
        )
    }

    fun logout() {
        Utils.setApiToken(null)
        Utils.setLastUserSession(null)
        redirectToLogin()
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
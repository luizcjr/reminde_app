package com.luiz.reminder.ui.activities.register

import android.content.Intent
import com.google.gson.JsonObject
import com.luiz.reminder.api.responses.RegisterResponse
import com.luiz.reminder.ui.activities.main.MainActivity
import com.luiz.reminder.ui.base.BaseViewModel
import com.luiz.reminder.util.Utils
import com.luiz.reminder.util.Utils.openActivity
import com.luiz.reminder.util.Utils.toast
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody


class RegisterViewModel : BaseViewModel() {

    val name = ValiFieldText().addNotEmptyValidator("Campo obrigatório!")
    val email = ValiFieldEmail().addEmailValidator("E-mail inválido!")
    val password =
        ValiFieldText().addMinLengthValidator("Senha curta! Mínimo seis caracteres.", 6)
    val form = ValiFiForm(email, name, password)

    private fun registerUser(): RequestBody {

        val json = JsonObject()
        json.addProperty("name", name.value)
        json.addProperty("email", email.value)
        json.addProperty("password", password.value)

        return RequestBody.create(MediaType.parse("application/json"), json.toString())
    }

    fun sendRegisterUser() {
        this.beforeRequest()

        disposable.add(
            apiRepository.register(registerUser())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RegisterResponse>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        afterRequest(e)
                    }

                    override fun onSuccess(t: RegisterResponse) {
                        afterRequest()

                        Utils.setApiToken(t.token)
                        Utils.setLastUserSession(t.user)
                        redirectToHome()
                    }
                })
        )
    }

    private fun redirectToHome() {
        context.toast("Cadastro efetuado com sucesso!")
        context.openActivity<MainActivity> {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun onCleared() {
        form.destroy()
        disposable.clear()
        super.onCleared()
    }
}
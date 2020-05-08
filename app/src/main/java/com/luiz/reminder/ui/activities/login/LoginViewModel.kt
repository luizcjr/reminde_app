package com.luiz.reminder.ui.activities.login

import android.content.Intent
import com.google.gson.JsonObject
import com.luiz.reminder.api.responses.LoginResponse
import com.luiz.reminder.ui.activities.forgot_passsword.ForgotPasswordActivity
import com.luiz.reminder.ui.activities.main.MainActivity
import com.luiz.reminder.ui.activities.register.RegisterActivity
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

class LoginViewModel : BaseViewModel() {

    val email = ValiFieldEmail().addEmailValidator("E-mail inválido!")
    val password =
        ValiFieldText().addMinLengthValidator("Senha curta!", 6)

    val form = ValiFiForm(email, password)

    private fun login(): RequestBody {

        val json = JsonObject()
        json.addProperty("email", email.value)
        json.addProperty("password", password.value)

        return RequestBody.create(MediaType.parse("application/json"), json.toString())
    }

    fun sendLogin() {
        this.beforeRequest()

        disposable.add(
            apiRepository.login(login())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginResponse>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        afterRequest(e)
                    }

                    override fun onSuccess(t: LoginResponse) {
                        afterRequest()

                        Utils.setApiToken(t.token)
                        Utils.setLastUserSession(t.user)
                        redirectToHome()
                    }
                })
        )
    }

    //Função para encaminhar para o cadastro
    fun register() {
        context.openActivity<RegisterActivity>()
    }

    //Função para encaminhar para a recuperação de senha
    fun forgotPassword() {
        context.openActivity<ForgotPasswordActivity>()
    }

    private fun redirectToHome() {
        context.openActivity<MainActivity> {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
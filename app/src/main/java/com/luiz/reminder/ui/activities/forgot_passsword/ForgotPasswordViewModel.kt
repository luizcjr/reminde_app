package com.luiz.reminder.ui.activities.forgot_passsword

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.luiz.reminder.R
import com.luiz.reminder.api.responses.LoginResponse
import com.luiz.reminder.ui.activities.login.LoginActivity
import com.luiz.reminder.ui.base.BaseViewModel
import com.luiz.reminder.util.Utils
import com.luiz.reminder.util.Utils.openActivity
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

class ForgotPasswordViewModel : BaseViewModel() {

    val email = ValiFieldEmail().addEmailValidator("E-mail inválido!")
    val form = ValiFiForm(email)

    private fun resetPassword(
        emailReset: String,
        tokenReset: String,
        passwordReset: String
    ): RequestBody {
        loading.value = true

        val json = JsonObject()
        json.addProperty("email", emailReset)
        json.addProperty("token", tokenReset)
        json.addProperty("password", passwordReset)

        return RequestBody.create(MediaType.parse("application/json"), json.toString())
    }

    fun sendResetPassword(
        emailReset: String,
        tokenReset: String,
        passwordReset: String
    ) {
        disposable.add(
            apiRepository.resetPassword(resetPassword(emailReset, tokenReset, passwordReset))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginResponse>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loadError.value = Utils.getMessageErrorObject(e)
                        loading.value = false
                    }

                    override fun onSuccess(t: LoginResponse) {
                        loadError.value = null
                        loading.value = false
                        redirectToLogin()
                    }
                })
        )
    }

    private fun forgotPassword(): RequestBody {
        loading.value = true

        val json = JsonObject()
        json.addProperty("email", email.value)

        return RequestBody.create(MediaType.parse("application/json"), json.toString())
    }

    fun sendToken() {
        disposable.add(
            apiRepository.sendToken(forgotPassword())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginResponse>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loadError.value = Utils.getMessageErrorObject(e)
                        loading.value = false
                    }

                    override fun onSuccess(t: LoginResponse) {
                        loadError.value = null
                        loading.value = false
                        Log.d("_res", "Sucesso")

                        dialogResetToken()
                    }
                })
        )
    }

    fun dialogResetToken() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        @SuppressLint("InflateParams")
        val v = Objects.requireNonNull(inflater).inflate(R.layout.dialog_reset_password, null)
        val alertDialogBuilder = android.app.AlertDialog.Builder(context)

        alertDialogBuilder.setView(v)
        val d = alertDialogBuilder.create()
        d.setCancelable(true)
        d.show()
        Objects.requireNonNull(d.window)!!
            .setBackgroundDrawableResource(android.R.color.transparent)

        val btnSend = d.findViewById(R.id.btnResetPass) as Button

        val etEmail = d.findViewById<TextInputEditText>(R.id.tietEmail)
        val etToken = d.findViewById<TextInputEditText>(R.id.tietToken)
        val etPassword = d.findViewById<TextInputEditText>(R.id.tietPassword)

        val tilEmail = d.findViewById<TextInputLayout>(R.id.tilEmail)
        val tilToken = d.findViewById<TextInputLayout>(R.id.tilToken)
        val tilPassword = d.findViewById<TextInputLayout>(R.id.tilPassword)

        etEmail.setText(email.value)

        btnSend.setOnClickListener {
            if (etEmail.text.isNullOrEmpty()) {
                tilEmail.error = "E-mail é obrigatório!"
            } else if (!Utils.emailValidator(etEmail.text.toString())) {
                tilEmail.error = "E-mail inválido!"
            } else if (etToken.text.isNullOrEmpty()) {
                tilToken.error = "Código é obrigatório!"
            } else if (etPassword.text.isNullOrEmpty()) {
                tilPassword.error = "Senha é obrigatório!"
            } else if (etPassword.text.toString().length < 6) {
                tilPassword.error = "Senha curta! Mínimo seis caracteres."
            } else {
                d.dismiss()
                sendResetPassword(
                    etEmail.text.toString(),
                    etToken.text.toString(),
                    etPassword.text.toString()
                )
            }
        }

        d.show()
    }

    private fun redirectToLogin() {
        context.openActivity<LoginActivity>()
    }

    override fun onCleared() {
        form.destroy()
        disposable.clear()
        super.onCleared()
    }
}
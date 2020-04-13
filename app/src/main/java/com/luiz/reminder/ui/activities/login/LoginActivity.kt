package com.luiz.reminder.ui.activities.login

import android.os.Bundle
import android.text.Html
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.R
import com.luiz.reminder.databinding.LoginBinding
import com.luiz.reminder.ui.base.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // data binding class
        val binding: LoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        //ViewModel
        loginViewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]

        //Texto com tag em html
        binding.tvRegister.text = Html.fromHtml(getString(R.string.title_sign_up_login))

        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        loginViewModel.loading.observe(this, loadingLiveDataObserver)
        loginViewModel.loadError.observe(this, errorLiveDataObserver)
        loginViewModel.context = this
    }

    //Quando o botão de retornar físico do aparelho é apertado, o aplicativo é fechado
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finishAndRemoveTask()
    }
}

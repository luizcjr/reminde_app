package com.luiz.reminder.ui.activities.login

import android.os.Bundle
import android.text.Html
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.BR
import com.luiz.reminder.R
import com.luiz.reminder.databinding.LoginBinding
import com.luiz.reminder.ui.base.BaseActivity

class LoginActivity : BaseActivity<LoginBinding, LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Texto com tag em html
        this.viewDataBinding().tvRegister.text = Html.fromHtml(getString(R.string.title_sign_up_login))

        this.viewModel().loading.observe(this, loadingLiveDataObserver)
        this.viewModel().loadError.observe(this, errorLiveDataObserver)
        this.viewModel().context = this
    }

    //Quando o botão de retornar físico do aparelho é apertado, o aplicativo é fechado
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finishAndRemoveTask()
    }

    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun binding(): Int {
        return BR.loginViewModel
    }

    override fun viewModel(): LoginViewModel {
        return ViewModelProviders.of(this)[LoginViewModel::class.java]
    }
}

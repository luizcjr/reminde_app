package com.luiz.reminder.ui.activities.forgot_passsword

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.BR
import com.luiz.reminder.R
import com.luiz.reminder.databinding.ForgotPasswordBinding
import com.luiz.reminder.ui.base.BaseActivity

class ForgotPasswordActivity : BaseActivity<ForgotPasswordBinding, ForgotPasswordViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.viewModel().loading.observe(this, loadingLiveDataObserver)
        this.viewModel().loadError.observe(this, errorLiveDataObserver)
        this.viewModel().context = this
    }

    override fun layoutId(): Int {
        return R.layout.activity_forgot_passsword
    }

    override fun binding(): Int {
        return BR.forgotViewModel
    }

    override fun viewModel(): ForgotPasswordViewModel {
        return ViewModelProviders.of(this)[ForgotPasswordViewModel::class.java]
    }
}

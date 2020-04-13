package com.luiz.reminder.ui.activities.forgot_passsword

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.R
import com.luiz.reminder.databinding.ForgotPasswordBinding
import com.luiz.reminder.ui.base.BaseActivity

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var viewModel: ForgotPasswordViewModel
    private var binding: ForgotPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // data binding class
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_passsword)

        //ViewModel
        viewModel = ViewModelProviders.of(this)[ForgotPasswordViewModel::class.java]

        binding!!.forgotViewModel = viewModel
        binding!!.lifecycleOwner = this

        viewModel.loading.observe(this, loadingLiveDataObserver)
        viewModel.loadError.observe(this, errorLiveDataObserver)
        viewModel.context = this
    }
}

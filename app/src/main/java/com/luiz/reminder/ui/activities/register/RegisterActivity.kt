package com.luiz.reminder.ui.activities.register

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.BR
import com.luiz.reminder.R
import com.luiz.reminder.databinding.RegisterBinding
import com.luiz.reminder.ui.base.BaseActivity

class RegisterActivity : BaseActivity<RegisterBinding, RegisterViewModel>() {

//    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // data binding class
//        val binding: RegisterBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_register)

        //ViewModel
//        viewModel = ViewModelProviders.of(this)[RegisterViewModel::class.java]
//
//        binding.registerViewModel = viewModel
//        binding.lifecycleOwner = this

        this.viewModel().loading.observe(this, loadingLiveDataObserver)
        this.viewModel().loadError.observe(this, errorLiveDataObserver)
        this.viewModel().context = this
    }

    override fun layoutId(): Int {
        return R.layout.activity_register
    }

    override fun binding(): Int {
        return BR.registerViewModel
    }

    override fun viewModel(): RegisterViewModel {
        return ViewModelProviders.of(this)[RegisterViewModel::class.java]
    }
}

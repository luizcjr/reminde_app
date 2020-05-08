package com.luiz.reminder.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.luiz.reminder.util.Utils

abstract class BaseActivity<T : ViewDataBinding, D : BaseViewModel> : AppCompatActivity() {

    private lateinit var mViewModel: D
    private lateinit var mViewDataBinding: T

    protected open fun initializeDatabinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId())
        mViewModel = viewModel()
        mViewDataBinding.setVariable(binding(), mViewModel)
        mViewDataBinding.executePendingBindings()
    }

    open fun viewDataBinding(): T {
        return mViewDataBinding
    }

    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun binding(): Int

    abstract fun viewModel(): D

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeDatabinding()
    }

    protected val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        if (isLoading) {
            Utils.onStartLoading(this)
        } else {
            Utils.onStopLoading()
        }
    }

    protected val errorLiveDataObserver = Observer<String> { error ->
        if (error != null) {
            Utils.alertError(error, this)
        }
    }

}
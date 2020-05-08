package com.luiz.reminder.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.luiz.reminder.api.models.User
import com.luiz.reminder.ui.activities.main.MainActivity
import com.luiz.reminder.util.Utils

abstract class BaseFragment<T : ViewDataBinding, D : BaseViewModel> : Fragment() {

    private lateinit var mViewModel: D
    private lateinit var mViewDataBinding: T
    private lateinit var mRootView: View

    protected open fun initializeDatabinding(inflater: LayoutInflater, container: ViewGroup) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mViewModel = viewModel()
        mViewDataBinding.setVariable(binding(), mViewModel)
        mViewDataBinding.executePendingBindings()
        mRootView = mViewDataBinding.root
    }

    open fun viewDataBinding(): T {
        return mViewDataBinding
    }

    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun binding(): Int

    abstract fun viewModel(): D

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        initializeDatabinding(inflater, container!!)

        return mRootView
    }

    protected val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        if (isLoading) {
            Utils.onStartLoading(context!!)
        } else {
            Utils.onStopLoading()
        }
    }

    protected val errorLiveDataObserver = Observer<String> { error ->
        if (error != null) {
            Utils.alertError(error, context!!)
        }
    }

    protected val userLiveDataObserver = Observer<User> { user ->
        user.let {
            getParentActivity()!!.title = user.name
        }
    }

    protected fun getParentActivity(): MainActivity? {
        return activity as MainActivity?
    }
}
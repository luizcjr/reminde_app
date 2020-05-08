package com.luiz.reminder.ui.activities.include_reminde

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.BR
import com.luiz.reminder.R
import com.luiz.reminder.databinding.IncludeRemindeBinding
import com.luiz.reminder.ui.base.BaseActivity
import com.luiz.reminder.util.Mask
import kotlinx.android.synthetic.main.toolbar.view.*

class IncludeRemindeActivity : BaseActivity<IncludeRemindeBinding, IncludeRemindeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.viewModel().loading.observe(this, loadingLiveDataObserver)
        this.viewModel().loadError.observe(this, errorLiveDataObserver)
        this.viewModel().context = this

        //Não consegui colocar a máscara pelo data binding
        this.viewDataBinding().etDate.addTextChangedListener(Mask.mask(this.viewDataBinding().etDate, Mask.FORMAT_DATE))
        this.viewDataBinding().etHour.addTextChangedListener(Mask.mask(this.viewDataBinding().etHour, Mask.FORMAT_HOUR))
    }

    override fun layoutId(): Int {
        return R.layout.activity_include_reminde
    }

    override fun binding(): Int {
        return BR.includeRemindeViewModel
    }

    override fun viewModel(): IncludeRemindeViewModel {
        return ViewModelProviders.of(this)[IncludeRemindeViewModel::class.java]
    }
}

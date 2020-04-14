package com.luiz.reminder.ui.activities.include_reminde

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.R
import com.luiz.reminder.databinding.IncludeRemindeBinding
import com.luiz.reminder.ui.base.BaseActivity
import com.luiz.reminder.util.Mask
import com.luiz.reminder.util.MaskWatcher

class IncludeRemindeActivity : BaseActivity() {

    private lateinit var viewModel: IncludeRemindeViewModel
    private var binding: IncludeRemindeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // data binding class
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_include_reminde)

        //ViewModel
        viewModel = ViewModelProviders.of(this)[IncludeRemindeViewModel::class.java]

        binding!!.includeRemindeViewModel = viewModel
        binding!!.lifecycleOwner = this
        binding!!.includeRemindeActivity = this

        viewModel.loading.observe(this, loadingLiveDataObserver)
        viewModel.loadError.observe(this, errorLiveDataObserver)
        viewModel.context = this

        //Não consegui colocar a máscara pelo data binding
        binding!!.etDate.addTextChangedListener(Mask.mask(binding!!.etDate, Mask.FORMAT_DATE))
        binding!!.etHour.addTextChangedListener(Mask.mask(binding!!.etHour, Mask.FORMAT_HOUR))
    }

    fun registerReminde() {
        viewModel.registerReminde(
            binding!!.etDate.text.toString(),
            binding!!.etHour.text.toString()
        )
    }
}

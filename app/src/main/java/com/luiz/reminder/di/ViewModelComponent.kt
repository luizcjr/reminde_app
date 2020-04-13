package com.luiz.reminder.di

import com.luiz.reminder.ui.base.BaseViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ViewModelComponent {
    fun inject(viewModel: BaseViewModel)
}
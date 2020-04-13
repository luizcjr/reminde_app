package com.luiz.reminder.di

import com.luiz.reminder.api.services.ApiRepository
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(repository: ApiRepository)
}
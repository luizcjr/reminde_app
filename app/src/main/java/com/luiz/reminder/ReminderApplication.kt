package com.luiz.reminder

import android.app.Application
import android.content.Context
import com.mlykotom.valifi.ValiFi

open class ReminderApplication : Application() {
    companion object {
        var context: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        /* Set instance */
        context = applicationContext

        ValiFi.install(this)
    }
}
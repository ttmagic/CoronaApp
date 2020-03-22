package com.ttmagic.corona

import android.content.Context
import com.base.mvvm.BaseApp

class App : BaseApp() {
    override fun appId(): String = BuildConfig.APPLICATION_ID
    override fun isDebug(): Boolean = BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context
    }
}
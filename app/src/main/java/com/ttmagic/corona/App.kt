package com.ttmagic.corona

import com.base.mvvm.BaseApp

class App : BaseApp() {
    override fun appId(): String = BuildConfig.APPLICATION_ID
    override fun isDebug(): Boolean = BuildConfig.DEBUG
}
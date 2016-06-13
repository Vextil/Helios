package io.vextil.launcher

import android.app.Application
import io.paperdb.Paper
import io.vextil.launcher.managers.WebAppManager
import io.vextil.launcher.models.App

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
    }

}
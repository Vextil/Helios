package io.vextil.launcher

import android.app.Application
import io.paperdb.Paper

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
    }

}
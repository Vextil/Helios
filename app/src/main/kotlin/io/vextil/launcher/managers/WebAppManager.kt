package io.vextil.launcher.managers

import io.paperdb.Paper
import io.vextil.launcher.models.App

class WebAppManager {

    private val book = Paper.book("web-apps")

    fun add(app: App) = book.write(app.activity, app)

    fun single(key: String) = book.read<App>(key)

    fun all(): List<App> {
        val apps = mutableListOf<App>()
        book.allKeys.forEach {
            apps.add(single(it))
        }
        return apps
    }

}

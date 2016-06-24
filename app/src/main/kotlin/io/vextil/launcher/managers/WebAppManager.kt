package io.vextil.launcher.managers

import android.content.Context
import io.paperdb.Paper
import io.vextil.launcher.R
import io.vextil.launcher.models.App

class WebAppManager(val context: Context) {

    private val book = Paper.book("web-apps")

    fun add(app: App) = book.write(app.name, app)

    fun single(key: String) = book.read<App>(key)

    fun all(): List<App> {
        val apps = mutableListOf<App>()
        book.allKeys.forEach {
            apps.add(single(it))
        }
        apps.add(App(
                name = "Control",
                pack = "io.vextil.launcher",
                activity = "http://guarana.duckdns.org",
                icon = context.getDrawable(R.drawable.ic_launcher),
                iconResource = R.drawable.ic_launcher,
                category = App.Category.APP
        ))
        return apps
    }

}

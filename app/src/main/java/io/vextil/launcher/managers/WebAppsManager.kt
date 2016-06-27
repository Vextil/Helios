package io.vextil.launcher.managers

import android.content.Context
import io.paperdb.Paper
import io.vextil.launcher.R
import io.vextil.launcher.models.AppModel

class WebAppsManager(val context: Context) {

    private val book = Paper.book("web-apps")
    private val apps = mutableListOf<AppModel>()

    fun add(app: AppModel) = book.write(app.name, app)

    fun single(key: String) = book.read<AppModel>(key)

    fun all(forceUpdate: Boolean = false): List<AppModel> {
        if (forceUpdate) fetch()
        return apps
    }

    fun fetch() {
        apps.clear()
        book.allKeys.forEach {
            apps.add(single(it))
        }
        apps.add(AppModel(
                name = "Control",
                pack = "io.vextil.launcher",
                activity = "http://guarana.duckdns.org",
                icon = context.getDrawable(R.drawable.ic_launcher),
                iconResource = R.drawable.ic_launcher,
                visible = true,
                category = AppModel.Category.APP
        ))
    }

}

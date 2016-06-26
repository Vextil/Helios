package io.vextil.launcher.managers

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import io.paperdb.Paper
import io.vextil.launcher.models.App

class AppsManager(val context: Context) {

    val packageManager = context.packageManager
    val apps = mutableListOf<App>()
    val hidden = Paper.book("hidden-apps")

    enum class FILTER {
        ALL, VISIBLE, HIDDEN
    }

    fun hide(app: App) {
        hidden.write(app.pack, app.pack)
        app.visible = false
    }

    fun show(app: App) {
        hidden.delete(app.pack)
        app.visible = true
    }

    fun isVisible(pack: String) = !hidden.exist(pack) && !pack.equals("io.vextil.launcher")

    fun all(filter: FILTER = FILTER.ALL, forceUpdate: Boolean = false): List<App> {
        if (forceUpdate) fetch()
        when (filter) {
            FILTER.ALL -> return apps
            FILTER.VISIBLE -> return apps.filter{ it.visible }
            FILTER.HIDDEN -> return apps.filter{ !it.visible }
        }
    }

    fun fetch() {
        apps.clear()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val launchables = packageManager.queryIntentActivities(intent, 0)

        launchables.forEach {
            val app = App(
                    name = it.loadLabel(packageManager).toString(),
                    pack = it.activityInfo.applicationInfo.packageName,
                    activity = it.activityInfo.name,
                    icon = it.activityInfo.loadIcon(packageManager),
                    iconResource = it.activityInfo.iconResource,
                    visible = isVisible(it.activityInfo.applicationInfo.packageName),
                    category = getCategory(it.activityInfo.applicationInfo)
            )
            apps.add(app)
        }
    }

    fun getCategory(info: ApplicationInfo): App.Category {
        if (info.flags.and(ApplicationInfo.FLAG_IS_GAME) == ApplicationInfo.FLAG_IS_GAME)
            return App.Category.GAME
        return App.Category.APP
    }

}
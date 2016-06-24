package io.vextil.launcher.managers

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import io.paperdb.Paper
import io.vextil.launcher.R
import io.vextil.launcher.models.App

class AppManager(val context: Context) {

    val packageManager = context.packageManager
    val hidden = Paper.book("hidden-apps")

    fun hide(app: App) = hidden.write(app.pack, app)

    fun show(app: App) = hidden.delete(app.pack)

    fun isHidden(app: App) = hidden.exist(app.pack) || app.pack.equals("io.vextil.launcher")

    fun all(): List<App> {
        val apps = mutableListOf<App>()
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
                    category = getAppCategory(it.activityInfo.applicationInfo)
            )
            if (!isHidden(app)) apps.add(app)
        }
        return apps
    }

    fun allHidden(): List<App> {
        val apps = mutableListOf<App>()
        hidden.allKeys.forEach {
            apps.add(hidden.read(it))
        }
        return apps
    }

    fun getAppCategory(info: ApplicationInfo): App.Category {
        if (info.flags.and(ApplicationInfo.FLAG_IS_GAME) == ApplicationInfo.FLAG_IS_GAME)
            return App.Category.GAME
        return App.Category.APP
    }

}
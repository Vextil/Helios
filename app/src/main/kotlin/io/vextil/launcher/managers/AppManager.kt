package io.vextil.launcher.managers

import android.content.Context
import android.content.Intent
import io.paperdb.Paper
import io.vextil.launcher.models.App

class AppManager(context: Context) {

    val packageManager = context.packageManager
    val hidden = Paper.book("hidden-apps")

    fun hide(app: App) = hidden.write(app.pack, app)

    fun show(app: App) = hidden.delete(app.pack)

    fun isHidden(app: App) = hidden.exist(app.pack)

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
                    iconResource = it.activityInfo.iconResource
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

}
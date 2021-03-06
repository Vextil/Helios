package io.vextil.launcher.managers

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import io.paperdb.Book
import io.paperdb.Paper
import io.vextil.launcher.models.AppModel

class AppsManager(val context: Context) {

    val packageManager: PackageManager = context.packageManager
    val apps = mutableListOf<AppModel>()
    val hidden: Book = Paper.book("hidden-apps")
    val locked: Book = Paper.book("locked-apps")

    enum class FILTER {
        ALL, VISIBLE, HIDDEN
    }

    fun hide(app: AppModel) {
        hidden.write(app.pack, app.pack)
        app.visible = false
    }

    fun show(app: AppModel) {
        hidden.delete(app.pack)
        app.visible = true
    }

    fun isVisible(pack: String) = !hidden.exist(pack) && !pack.equals("io.vextil.launcher")

    fun lock(app: AppModel) {
        locked.write(app.pack, app.pack)
        app.locked = true
    }

    fun unlock(app: AppModel) {
        locked.delete(app.pack)
        app.locked = false
    }

    fun isLocked(pack: String) = locked.exist(pack)

    fun all(filter: FILTER = FILTER.ALL, forceUpdate: Boolean = false): List<AppModel> {
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
            val app = AppModel(
                    name = it.loadLabel(packageManager).toString(),
                    pack = it.activityInfo.applicationInfo.packageName,
                    activity = it.activityInfo.name,
                    icon = it.activityInfo.loadIcon(packageManager),
                    iconResource = it.activityInfo.iconResource,
                    visible = isVisible(it.activityInfo.applicationInfo.packageName),
                    locked = isLocked(it.activityInfo.applicationInfo.packageName),
                    category = getCategory(it.activityInfo.applicationInfo)
            )
            apps.add(app)
        }
    }

    fun getCategory(info: ApplicationInfo): AppModel.Category {
        if (info.flags.and(ApplicationInfo.FLAG_IS_GAME) == ApplicationInfo.FLAG_IS_GAME)
            return AppModel.Category.GAME
        return AppModel.Category.APP
    }

}
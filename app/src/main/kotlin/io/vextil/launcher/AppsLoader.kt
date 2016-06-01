package io.vextil.launcher

import android.content.AsyncTaskLoader
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import io.vextil.launcher.Models.AppModel

class AppsLoader(context: Context) : AsyncTaskLoader<List<AppModel>>(context) {

    var apps: List<AppModel> = listOf()
    val packageManager = context.packageManager
    val appChangesObserver = AppChangesReceiver(this)
    val appHider = AppHider(context)

    override fun loadInBackground(): List<AppModel> {
        val apps = mutableListOf<AppModel>()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val launchables = packageManager.queryIntentActivities(intent, 0)

        for (r: ResolveInfo in launchables) {
            val packageName = r.activityInfo.applicationInfo.packageName
            if (!appHider.isHidden(packageName)) {
                val app = AppModel(
                    name = r.loadLabel(packageManager).toString(),
                    pack = packageName,
                    activity = r.activityInfo.name,
                    icon = r.activityInfo.loadIcon(packageManager)
                )
                apps.add(app)
            }
        }
        apps.sortBy { it.name }
        return apps
    }

    override fun deliverResult(apps: List<AppModel>) {
        if (isStarted) super.deliverResult(apps)
        this.apps = apps
    }

    override fun onStartLoading() {
        if (apps.isNotEmpty()) deliverResult(apps)
        if (takeContentChanged() || apps.isEmpty()) forceLoad()
    }

    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onReset() {
        super.onReset()
        onStopLoading()
        context.unregisterReceiver(appChangesObserver)
    }

}

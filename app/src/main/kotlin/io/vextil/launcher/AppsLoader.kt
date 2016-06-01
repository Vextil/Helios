package io.vextil.launcher

import android.content.AsyncTaskLoader
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo

class AppsLoader(context: Context) : AsyncTaskLoader<List<AppDetail>>(context) {

    var apps: List<AppDetail> = listOf()
    val packageManager = context.packageManager
    val appChangesObserver = AppChangesReceiver(this)
    val appHider = AppHider(context)

    override fun loadInBackground(): List<AppDetail> {
        val apps = mutableListOf<AppDetail>()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        val launchables = packageManager.queryIntentActivities(i, 0)

        for (r: ResolveInfo in launchables) {
            val pack = r.activityInfo.applicationInfo.packageName
            if (!appHider.isHidden(pack)) {
                val app = AppDetail()
                app.name = r.loadLabel(packageManager).toString()
                app.pack = pack
                app.activity = r.activityInfo.name
                app.icon = r.activityInfo.loadIcon(packageManager)
                apps.add(app)
            }
        }
        apps.sortBy { it.name }
        return apps
    }

    override fun deliverResult(apps: List<AppDetail>) {
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

package io.vextil.launcher.async

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import io.vextil.launcher.managers.AppsManager
import io.vextil.launcher.managers.WebAppsManager
import io.vextil.launcher.models.AppModel

class AppsAsyncLoader(val appsManager: AppsManager, val webAppsManager: WebAppsManager, context: Context) : AsyncTaskLoader<List<AppModel>>(context) {

    var apps: List<AppModel> = listOf()
    val appChangesObserver = AppChangesReceiver(this)

    override fun loadInBackground(): List<AppModel> {
        val apps = mutableListOf<AppModel>()
        apps.addAll(appsManager.all(AppsManager.FILTER.VISIBLE, true))
        apps.addAll(webAppsManager.all(true))
        apps.sortBy { it.name }
        return apps
    }

    override fun onForceLoad() {
        apps = loadInBackground()
        deliverResult(apps)
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

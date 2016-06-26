package io.vextil.launcher

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import io.vextil.launcher.managers.AppManager
import io.vextil.launcher.managers.WebAppManager
import io.vextil.launcher.models.App

class AppsAsyncLoader(context: Context) : AsyncTaskLoader<List<App>>(context) {

    var apps: List<App> = listOf()
    val appChangesObserver = AppChangesReceiver(this)
    val appFetcher = AppManager(context)
    val webAppFetcher = WebAppManager(context)

    override fun loadInBackground(): List<App> {
        val apps = mutableListOf<App>()
        apps.addAll(appFetcher.all())
        apps.addAll(webAppFetcher.all())
        apps.sortBy { it.name }
        return apps
    }

    override fun deliverResult(apps: List<App>) {
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

package io.vextil.launcher

import android.content.AsyncTaskLoader
import android.content.Context
import android.content.Intent
import io.vextil.launcher.models.AppModel

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

        launchables.forEach {
            val packageName = it.activityInfo.applicationInfo.packageName
            if (!appHider.isHidden(packageName)) {
                val app = AppModel(
                    name = it.loadLabel(packageManager).toString(),
                    pack = packageName,
                    activity = it.activityInfo.name,
                    icon = it.activityInfo.loadIcon(packageManager),
                    iconResource = it.activityInfo.iconResource
                )
                apps.add(app)
            }
        }
        apps.add(AppModel("Control", "io.vextil.launcher", "https://androidpolice.com", context.getDrawable(R.drawable.ic_launcher), R.drawable.ic_launcher))
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

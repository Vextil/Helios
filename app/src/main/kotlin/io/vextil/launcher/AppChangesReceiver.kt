package io.vextil.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class AppChangesReceiver(val loader: AppsLoader): BroadcastReceiver() {

    val filter = IntentFilter(Intent.ACTION_PACKAGE_ADDED)
    val sdFilter = IntentFilter()

    init {
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED)
        filter.addDataScheme("package")
        loader.context.registerReceiver(this, filter)

        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)
        loader.context.registerReceiver(this, sdFilter)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        loader.onContentChanged()
    }

}
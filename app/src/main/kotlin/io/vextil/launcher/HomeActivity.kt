package io.vextil.launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appHider = AppHider(this)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        setContentView(R.layout.activity_home)

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

        recycler.layoutManager = GridLayoutManager(this, 4)
        recycler.adapter = AppsAdapter(this, apps) { component ->
            val intent = Intent(Intent.ACTION_MAIN)
            intent.component = component
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {}

}

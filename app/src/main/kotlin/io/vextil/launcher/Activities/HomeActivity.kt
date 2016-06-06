package io.vextil.launcher.activities

import android.app.Activity
import android.app.LoaderManager.LoaderCallbacks
import android.content.ComponentName
import android.content.Intent
import android.content.Loader
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import io.vextil.launcher.*
import io.vextil.launcher.adapters.AppsAdapter
import io.vextil.launcher.models.App
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.properties.Delegates

class HomeActivity(): Activity(), LoaderCallbacks<List<App>> {

    var adapter = AppsAdapter(this)
    var loader: AppsLoader by Delegates.notNull()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<App>> {
        loader = AppsLoader(this)
        return loader
    }

    override fun onLoadFinished(loader: Loader<List<App>>, data: List<App>) {
        adapter.swapData(data)
    }

    override fun onLoaderReset(loader: Loader<List<App>>?) {
        throw UnsupportedOperationException()
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(0, null, this)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        setContentView(R.layout.activity_home)
        val appHider = AppHider(this)

        recycler.layoutManager = GridLayoutManager(this, 4)
        adapter.setOnClickListener {
            if (it.pack.equals("io.vextil.launcher")) {
                val intent = Intent(this, WebAppActivity::class.java);
                intent.putExtra("TITLE", it.name)
                intent.putExtra("ICON", it.iconResource)
                intent.putExtra("WEB-URL", it.activity)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                startActivity(intent);
            } else {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.component = ComponentName(it.pack, it.activity)
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                startActivity(intent)
            }
        }
        adapter.setOnLongClickListener {
            appHider.add(it.pack)
            loader.onContentChanged()
        }
        recycler.adapter = adapter
    }

    override fun onBackPressed() {}

}

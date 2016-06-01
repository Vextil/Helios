package io.vextil.launcher

import android.app.Activity
import android.app.LoaderManager.LoaderCallbacks
import android.content.Intent
import android.content.Loader
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.properties.Delegates

class HomeActivity() : Activity(), LoaderCallbacks<List<AppDetail>> {

    var adapter = AppsAdapter(this)
    var loader: AppsLoader by Delegates.notNull()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<AppDetail>> {
        loader = AppsLoader(this)
        return loader
    }

    override fun onLoadFinished(loader: Loader<List<AppDetail>>, data: List<AppDetail>) {
        (recycler.adapter as AppsAdapter).swapData(data)
    }

    override fun onLoaderReset(loader: Loader<List<AppDetail>>?) {
        throw UnsupportedOperationException()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(0, null, this)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        setContentView(R.layout.activity_home)
        val appHider = AppHider(this)

        recycler.layoutManager = GridLayoutManager(this, 4)
        adapter.setOnClickListener { component ->
            val intent = Intent(Intent.ACTION_MAIN)
            intent.component = component
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            startActivity(intent)
        }
        adapter.setOnLongClickListener { component ->
            appHider.add(component.packageName)
            loader.onContentChanged()
        }
        recycler.adapter = adapter
    }

    override fun onBackPressed() {}

}

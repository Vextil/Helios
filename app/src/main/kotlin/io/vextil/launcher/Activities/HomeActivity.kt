package io.vextil.launcher.activities

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.GridLayoutManager
import android.view.View
import io.vextil.launcher.*
import io.vextil.launcher.adapters.LauncherAdapter
import io.vextil.launcher.models.App
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.properties.Delegates

class HomeActivity(): AppCompatActivity(), LoaderManager.LoaderCallbacks<List<App>> {

    var adapter = LauncherAdapter(this)
    var loader: AppsAsyncLoader by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportLoaderManager.initLoader(0, null, this)

        setUpContent()
        setUpToolbar()
        setUpDrawer()
        setUpWallpaper()
    }

    fun setUpContent() {
        setContentView(R.layout.activity_home)
        recycler.layoutManager = GridLayoutManager(this, 4)
        adapter.setOnClickListener { view, app ->
            if (app.pack.equals("io.vextil.launcher")) {
                val intent = Intent(this, WebAppActivity::class.java)
                intent.putExtra("TITLE", app.name)
                intent.putExtra("ICON", app.iconResource)
                intent.putExtra("WEB-URL", app.activity)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                startActivity(intent);
            } else {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.component = ComponentName(app.pack, app.activity)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                startActivity(intent)
            }
        }
        adapter.setOnLongClickListener {
            loader.onContentChanged()
        }
        recycler.adapter = adapter
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0)
            statusBarHeight= resources.getDimensionPixelSize(resourceId)
        toolbar.setPadding(0, statusBarHeight, 0, 0)
    }

    fun setUpDrawer(){
        val toolbarDrawerToggle = ActionBarDrawerToggle(this, drawer, toolbar, 0, 0)
        drawer.addDrawerListener(toolbarDrawerToggle)
        toolbarDrawerToggle.syncState()
    }

    fun setUpWallpaper(){
        val wallpaperManager = WallpaperManager.getInstance(this)
        main.background = wallpaperManager.drawable
        Palette.from((wallpaperManager.drawable as BitmapDrawable).bitmap).generate(){
            toolbar.background = ColorDrawable(it.getVibrantColor(0))
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<App>> {
        loader = AppsAsyncLoader(this)
        return loader
    }

    override fun onLoadFinished(loader: Loader<List<App>>, data: List<App>) {
        adapter.swapData(data)
    }

    override fun onLoaderReset(loader: Loader<List<App>>?) {
        throw UnsupportedOperationException()
    }

    override fun onBackPressed() {}

}

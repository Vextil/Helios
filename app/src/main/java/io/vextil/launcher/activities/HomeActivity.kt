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
import android.widget.Toast
import com.mattprecious.swirl.SwirlView
import com.mtramin.rxfingerprint.RxFingerprint
import com.mtramin.rxfingerprint.data.FingerprintResult
import io.vextil.launcher.*
import io.vextil.launcher.activities.settings.AppSettingsActivity
import io.vextil.launcher.activities.settings.WebAppSettingsActivity
import io.vextil.launcher.adapters.LauncherAdapter
import io.vextil.launcher.async.AppsAsyncLoader
import io.vextil.launcher.models.AppModel
import kotlinx.android.synthetic.main.activity_home.*
import rx.Subscription
import kotlin.properties.Delegates

class HomeActivity(): AppCompatActivity(), LoaderManager.LoaderCallbacks<List<AppModel>> {

    var adapter = LauncherAdapter(this)
    var loader: AppsAsyncLoader by Delegates.notNull()
    var wallpaperManager: WallpaperManager by Delegates.notNull()
    var fingerprintSubscription: Subscription? = null
    var shouldUpdateToolbarColor = false
    var shouldRefreshApps = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wallpaperManager = WallpaperManager.getInstance(this)
        supportLoaderManager.initLoader(0, null, this)

        setUpContent()
        setUpToolbar()
        setUpDrawer()
    }

    override fun onResume() {
        super.onResume()
        if (shouldUpdateToolbarColor){
            shouldUpdateToolbarColor = false
            updateToolbarColor()
        }
        if (shouldRefreshApps){
            shouldRefreshApps = false
            loader.onContentChanged()
        }
    }

    override fun onPause() {
        cancelLaunchAppOrWebsiteLocked()
        super.onPause()
    }

    fun setUpContent() {
        setContentView(R.layout.activity_home)
        title = Helios.settings.getString("header-text", "Helios")
        recycler.layoutManager = GridLayoutManager(this, 4)
        adapter.setOnClickListener { view, app ->
            if (app.locked) {
                launchAppOrWebsiteLocked(app)
            } else {
                launchAppOrWebsite(app)
            }
        }
        adapter.setOnLongClickListener {
            loader.onContentChanged()
        }
        recycler.adapter = adapter

        cancelFingerprintButton.setOnClickListener {
            cancelLaunchAppOrWebsiteLocked()
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        // Calculate Status Bar height
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0)
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        // Apply the Status Bar height as the Toolbar's top padding
        // This allows the Toolbar to show behind the Status Bar, "sharing" the color
        toolbar.setPadding(0, statusBarHeight, 0, 0)
        updateToolbarColor()
    }

    fun setUpDrawer(){
        val toolbarDrawerToggle = ActionBarDrawerToggle(this, drawer, toolbar, 0, 0)
        drawer.addDrawerListener(toolbarDrawerToggle)
        toolbarDrawerToggle.syncState()
        navigation.setNavigationItemSelectedListener {
            drawer.closeDrawers()
            var intent: Intent
            if (it.title.equals("Wallpapers")) {
                shouldUpdateToolbarColor = true
                intent = Intent(Intent.ACTION_SET_WALLPAPER)
                intent = Intent.createChooser(intent, "Select Wallpaper")
            } else if (it.title.equals("App Hider")) {
                shouldRefreshApps = true
                intent = Intent(this, AppSettingsActivity::class.java)
            } else {
                intent = Intent(this, WebAppSettingsActivity::class.java)
            }
            startActivity(intent)
            true
        }
    }

    fun updateToolbarColor() {
        Palette.from((wallpaperManager.drawable as BitmapDrawable).bitmap).generate(){
            toolbar.background = ColorDrawable(it.getVibrantColor(0))
        }
    }

    fun launchAppOrWebsite(app: AppModel) {
        if (app.pack.equals("io.vextil.launcher")) {
            launchWebsite(app)
        } else {
            launchApp(app)
        }
    }

    fun launchApp(app: AppModel) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.component = ComponentName(app.pack, app.activity)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        startActivity(intent)
    }

    fun launchWebsite(app: AppModel) {
        val intent = Intent(this, WebAppActivity::class.java)
        intent.putExtra("TITLE", app.name)
        intent.putExtra("ICON", app.iconResource)
        intent.putExtra("WEB-URL", app.activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        startActivity(intent)
    }

    fun launchAppOrWebsiteLocked(app: AppModel) {
        overlay.visibility = View.VISIBLE
        fingerprintIcon.setState(SwirlView.State.ON)
        fingerprintSubscription = RxFingerprint.authenticate(this).subscribe(
                {
                    when (it.result) {
                        FingerprintResult.AUTHENTICATED -> {
                            fingerprintIcon.setState(SwirlView.State.OFF)
                            overlay.visibility = View.GONE
                            launchAppOrWebsite(app)
                        }
                        FingerprintResult.FAILED -> fingerprintIcon.setState(SwirlView.State.ERROR)
                        FingerprintResult.HELP -> fingerprintIcon.setState(SwirlView.State.ERROR)
                        else -> {
                            cancelLaunchAppOrWebsiteLocked()
                        }
                    }
                },
                {
                    Toast.makeText(this, "FINGERPRINT AUTH FUCKED UP", Toast.LENGTH_LONG).show()
                }
        )
    }

    fun cancelLaunchAppOrWebsiteLocked() {
        fingerprintIcon.setState(SwirlView.State.OFF)
        fingerprintSubscription?.unsubscribe()
        overlay.visibility = View.GONE
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<AppModel>> {
        loader = AppsAsyncLoader(Helios.apps, Helios.webApps, this)
        return loader
    }

    override fun onLoadFinished(loader: Loader<List<AppModel>>, data: List<AppModel>) {
        adapter.swapData(data)
    }

    override fun onLoaderReset(loader: Loader<List<AppModel>>?) {
        throw UnsupportedOperationException()
    }

    override fun onBackPressed() {
        cancelLaunchAppOrWebsiteLocked()
    }

}

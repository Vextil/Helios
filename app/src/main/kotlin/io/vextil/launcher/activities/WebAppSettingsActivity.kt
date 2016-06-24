package io.vextil.launcher.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.vextil.launcher.R
import io.vextil.launcher.adapters.WebAppsAdapter
import io.vextil.launcher.managers.WebAppManager
import kotlinx.android.synthetic.main.activity_web_app_settings.*

class WebAppSettingsActivity : AppCompatActivity() {

    val webApps = WebAppManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_app_settings)
        setSupportActionBar(toolbar)
        val adapter = WebAppsAdapter(this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = WebAppsAdapter(this)
        adapter.swapData(webApps.all())
    }
}

package io.vextil.launcher.activities.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.vextil.launcher.Helios
import io.vextil.launcher.R
import io.vextil.launcher.adapters.AppHiderAdapter
import io.vextil.launcher.managers.AppsManager
import kotlinx.android.synthetic.main.activity_web_app_settings.*

class AppSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_app_settings)
        setSupportActionBar(toolbar)
        title = "App Hider"
        val adapter = AppHiderAdapter(Helios.apps, this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}

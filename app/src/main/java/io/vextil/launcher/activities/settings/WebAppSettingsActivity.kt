package io.vextil.launcher.activities.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.vextil.launcher.Application
import io.vextil.launcher.R
import io.vextil.launcher.adapters.WebAppsAdapter
import kotlinx.android.synthetic.main.activity_web_app_settings.*

class WebAppSettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_app_settings)
        setSupportActionBar(toolbar)
        title = "Web Apps"
        val adapter = WebAppsAdapter(this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.swapData(Application.webAppsManager.all())
    }
}
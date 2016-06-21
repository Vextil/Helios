package io.vextil.launcher.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.vextil.launcher.R
import kotlinx.android.synthetic.main.activity_web_app_settings.*

class WebAppSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_app_settings)
        setSupportActionBar(toolbar)

    }
}

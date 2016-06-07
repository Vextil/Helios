package io.vextil.launcher.fragments.settings

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.vextil.launcher.R
import kotlinx.android.synthetic.main.fragment_settings_apps.*

class AppsSettingsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_settings_apps, container, false)
        return view
    }

}
package io.vextil.launcher.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import io.vextil.launcher.R
import io.vextil.launcher.managers.AppsManager
import io.vextil.launcher.models.App
import kotlinx.android.synthetic.main.app_settings_list_item.view.*

class AppHiderAdapter(val appsManager: AppsManager, context: Context): BaseAppsAdapter(context) {

    init {
        swapData(appsManager.all())
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.app_settings_list_item, parent, false)
    }

    override fun onBindData(view: View, app: App) {
        view.name.text = app.name
        view.icon.setImageDrawable(app.icon)
        view.toggle.isChecked = app.visible

        view.toggle.setOnClickListener() { it as Switch
            if (it.isChecked) {
                appsManager.show(app)
                it.isChecked = true
            } else {
                appsManager.hide(app)
                it.isChecked = false
            }
        }
    }

}
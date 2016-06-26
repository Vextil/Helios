package io.vextil.launcher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.vextil.launcher.R
import io.vextil.launcher.models.App
import kotlinx.android.synthetic.main.webapp_settings_list_item.view.*

class WebAppsAdapter(context: Context): BaseAppsAdapter(context) {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.webapp_settings_list_item, parent, false)
    }

    override fun onBindData(view: View, app: App) {
        view.name.text = app.name
        view.url.text = app.activity
        view.icon.setImageDrawable(app.icon)
    }
}
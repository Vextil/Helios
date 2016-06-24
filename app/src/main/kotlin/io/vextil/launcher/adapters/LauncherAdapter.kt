package io.vextil.launcher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.vextil.launcher.models.App
import io.vextil.launcher.R
import kotlinx.android.synthetic.main.app_grid_item.view.*

class LauncherAdapter(context: Context) : BaseAppsAdapter(context) {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.app_grid_item, parent, false)
    }

    override fun onBindData(view: View, app: App) {
        view.item_app_icon.setImageDrawable(app.icon)
    }

}
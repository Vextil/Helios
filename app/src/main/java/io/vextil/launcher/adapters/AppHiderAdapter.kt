package io.vextil.launcher.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import io.vextil.launcher.R
import io.vextil.launcher.managers.AppsManager
import io.vextil.launcher.models.AppModel
import kotlinx.android.synthetic.main.app_settings_list_item.view.*

class AppHiderAdapter(val appsManager: AppsManager, context: Context): BaseAppsAdapter(context) {

    init {
        swapData(appsManager.all())
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.app_settings_list_item, parent, false)
    }

    override fun onBindData(view: View, app: AppModel) {
        view.name.text = app.name
        view.icon.setImageDrawable(app.icon)

        view.delete.setOnClickListener() {
            val intent = Intent(Intent.ACTION_DELETE)
            intent.data = Uri.parse("package:" + app.pack)
            context.startActivity(intent)
        }

        view.hiddenToggle.isChecked = !app.visible
        view.hiddenToggle.setOnClickListener() { it as Switch
            if (it.isChecked) {
                appsManager.hide(app)
            } else {
                appsManager.show(app)
            }
        }

        view.lockedToggle.isChecked = app.locked
        view.lockedToggle.setOnClickListener() { it as Switch
            if (it.isChecked) {
                appsManager.lock(app)
            } else {
                appsManager.unlock(app)
            }
        }
    }

}
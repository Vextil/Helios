package io.vextil.launcher.adapters

import android.content.ComponentName
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.vextil.launcher.models.App
import io.vextil.launcher.R
import kotlinx.android.synthetic.main.app_grid_item.view.*

class LauncherAdapter(val context: Context) : RecyclerView.Adapter<LauncherAdapter.ViewHolder>() {

    var apps = listOf<App>()
    var itemClick: (App) -> Unit = {}
    var itemLongClick: (App) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(context).inflate(R.layout.app_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) = holder.bindData(apps[pos])

    override fun getItemCount() = apps.size

    fun swapData(data: List<App>) {
        apps = data
        notifyDataSetChanged()
    }

    fun setOnClickListener (itemClick: (App) -> Unit) {
        this.itemClick = itemClick
    }

    fun setOnLongClickListener(itemLongClick: ((App) -> Unit)) {
        this.itemLongClick = itemLongClick
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindData(app : App) {
            view.item_app_icon.setImageDrawable(app.icon)
            view.setOnClickListener { itemClick(app) }
            view.setOnLongClickListener { itemLongClick(app);true }
        }
    }

}
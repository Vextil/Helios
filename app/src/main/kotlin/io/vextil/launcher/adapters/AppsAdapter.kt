package io.vextil.launcher.adapters

import android.content.ComponentName
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.vextil.launcher.models.AppModel
import io.vextil.launcher.R
import kotlinx.android.synthetic.main.app_grid_item.view.*

class AppsAdapter(val context: Context) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    var apps = listOf<AppModel>()
    var itemClick: (AppModel) -> Unit = {}
    var itemLongClick: (AppModel) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(context).inflate(R.layout.app_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) = holder.bindData(apps[pos])

    override fun getItemCount() = apps.size

    fun swapData(data: List<AppModel>) {
        apps = data
        notifyDataSetChanged()
    }

    fun setOnClickListener (itemClick: (AppModel) -> Unit) {
        this.itemClick = itemClick
    }

    fun setOnLongClickListener(itemLongClick: ((AppModel) -> Unit)) {
        this.itemLongClick = itemLongClick
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindData(app : AppModel) {
            view.item_app_icon.setImageDrawable(app.icon)
            view.setOnClickListener { itemClick(app) }
            view.setOnLongClickListener { itemLongClick(app);true }
        }
    }

}
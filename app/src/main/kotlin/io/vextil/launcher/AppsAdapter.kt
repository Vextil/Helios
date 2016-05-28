package io.vextil.launcher

import android.content.ComponentName
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.app_grid_item.view.*

class AppsAdapter(val context: Context, var apps: List<AppDetail>,
                  val itemClick: (ComponentName) -> Unit) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(context).inflate(R.layout.app_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) = holder.bindData(apps[pos])

    override fun getItemCount() = apps.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindData(app : AppDetail) {
            itemView.item_app_icon.setImageDrawable(app.icon)
            val component = ComponentName(app.pack, app.activity)
            itemView.setOnClickListener { itemClick(component) }
        }
    }

}
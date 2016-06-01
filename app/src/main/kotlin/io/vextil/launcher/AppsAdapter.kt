package io.vextil.launcher

import android.content.ComponentName
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.app_grid_item.view.*
import kotlin.properties.Delegates

class AppsAdapter(val context: Context) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    var apps = listOf<AppDetail>()
    var itemClick: (ComponentName) -> Unit by Delegates.notNull()
    var itemLongClick: (ComponentName) -> Unit by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(context).inflate(R.layout.app_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) = holder.bindData(apps[pos])

    override fun getItemCount() = apps.size

    fun swapData(data: List<AppDetail>) {
        apps = data
        notifyDataSetChanged()
    }

    fun setOnClickListener (itemClick: (ComponentName) -> Unit) {
        this.itemClick = itemClick
    }

    fun setOnLongClickListener(itemLongClick: ((ComponentName) -> Unit)) {
        this.itemLongClick = itemLongClick
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindData(app : AppDetail) {
            itemView.item_app_icon.setImageDrawable(app.icon)
            val component = ComponentName(app.pack, app.activity)
            itemView.setOnClickListener { itemClick(component) }
            itemView.setOnLongClickListener { itemLongClick(component);true }
        }
    }

}
package io.vextil.launcher.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.vextil.launcher.models.AppModel

abstract class BaseAppsAdapter(val context: Context): RecyclerView.Adapter<BaseAppsAdapter.ViewHolder>() {

    var apps = listOf<AppModel>()
    var itemClick: (View, AppModel) -> Unit = { view, app -> }
    var itemLongClick: (AppModel) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = onCreateView(LayoutInflater.from(context), parent)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(apps[position])
    }

    override fun getItemCount() = apps.size

    fun swapData(data: List<AppModel>) {
        apps = data
        notifyDataSetChanged()
    }

    fun setOnClickListener (itemClick: (View, AppModel) -> Unit) {
        this.itemClick = itemClick
    }

    fun setOnLongClickListener(itemLongClick: ((AppModel) -> Unit)) {
        this.itemLongClick = itemLongClick
    }

    abstract fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View

    abstract fun onBindData(view: View, app: AppModel)

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindData(app: AppModel) {
            onBindData(view, app)
            view.setOnClickListener { itemClick(view, app) }
            view.setOnLongClickListener { itemLongClick(app);true }
        }
    }
}
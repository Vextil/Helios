package io.vextil.launcher.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.vextil.launcher.R

class AppsListAdapter(context: Context): RecyclerView.Adapter<AppsListAdapter.ViewHolder>() {

    val inflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        throw UnsupportedOperationException()
    }

    override fun getItemCount(): Int {
        throw UnsupportedOperationException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.fragment_settings_apps, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    }

}

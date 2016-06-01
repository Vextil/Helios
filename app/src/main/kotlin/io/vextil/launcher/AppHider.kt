package io.vextil.launcher

import android.content.Context

class AppHider(context: Context) {

    val prefs = context.getSharedPreferences("hidden_apps", Context.MODE_PRIVATE)

    fun isHidden(pack: String) : Boolean {
        return prefs.contains(pack) || pack == "io.vextil.launcher"
    }

    fun add(pack: String) {
        prefs.edit().putBoolean(pack, true).apply()
    }

    fun remove(pack: String) {
        prefs.edit().remove(pack).apply()
    }

}

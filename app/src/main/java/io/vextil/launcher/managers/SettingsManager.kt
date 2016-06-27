package io.vextil.launcher.managers

import android.content.Context
import io.paperdb.Paper

class SettingsManager(context: Context) {

    val strings = Paper.book("settings-strings")
    val ints = Paper.book("settings-ints")
    val bools = Paper.book("settings-bools")

    fun getString(key: String, default: String = ""): String = strings.read(key, default)
    fun getInt(key: String, default: Int = 0): Int = ints.read(key, default)
    fun getBool(key: String, default: Boolean = false): Boolean = bools.read(key, default)

    fun set(key: String, value: String) = strings.write(key, value)
    fun set(key: String, value: Int) = ints.write(key, value)
    fun set(key: String, value: Boolean) = bools.write(key, value)


}
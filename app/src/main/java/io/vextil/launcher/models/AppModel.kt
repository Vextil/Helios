package io.vextil.launcher.models

import android.graphics.drawable.Drawable

data class AppModel(
        val name: String,
        val pack: String,
        val activity: String,
        val icon: Drawable,
        val iconResource: Int,
        var locked: Boolean,
        var visible: Boolean,
        val category: Category = Category.APP
) {
    enum class Category {
        APP, GAME
    }
}
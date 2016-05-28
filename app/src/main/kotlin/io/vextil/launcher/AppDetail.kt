package io.vextil.launcher

import android.graphics.drawable.Drawable
import kotlin.properties.Delegates

class AppDetail {
    var name : String = ""
    var pack : String = ""
    var activity : String = ""
    var icon : Drawable by Delegates.notNull()
}
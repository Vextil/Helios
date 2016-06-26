package io.vextil.launcher.activities

import android.app.Activity
import android.app.ActivityManager.TaskDescription
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import io.vextil.launcher.*
import kotlinx.android.synthetic.main.activity_web.*
import kotlin.properties.Delegates

class WebAppActivity: Activity() {

    var title: String by Delegates.notNull()
    var icon: Bitmap by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        title =  intent.extras.getString("TITLE")
        icon = BitmapFactory.decodeResource(resources, intent.extras.getInt("ICON"))

        setTaskDescription(TaskDescription(title, icon))

        CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true)
        webview.setWebViewClient(WebViewClient())
        webview.settings.javaScriptEnabled = true

        webview.loadUrl(intent.extras.getString("WEB-URL"))
    }

    inner class WebViewClient: android.webkit.WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            view.evaluateJavascript(
                    "(function() { return (document.querySelectorAll('[name=theme-color]')[0].getAttribute('content')); })();"
            ) { setTaskDescription(TaskDescription(title, icon, Color.parseColor(it.substring(1,8)))) }
        }
    }

}
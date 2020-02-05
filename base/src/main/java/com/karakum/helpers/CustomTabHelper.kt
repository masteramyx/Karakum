package com.karakum.helpers

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

object CustomTabHelper {

    @JvmStatic
    fun launchCustomTab(url: String, context: Context){
        val builder = CustomTabsIntent.Builder()

        builder.addDefaultShareMenuItem()
        builder.setShowTitle(true)
        builder.setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
        builder.build().apply {
            launchUrl(context, Uri.parse(url))
        }
    }
}
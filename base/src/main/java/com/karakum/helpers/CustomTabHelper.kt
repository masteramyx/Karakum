package com.karakum.helpers

import androidx.browser.customtabs.CustomTabsIntent

object CustomTabHelper {

    @JvmStatic
    fun launchCustomTab(url: String){
        val builder = CustomTabsIntent.Builder()

        builder.setShowTitle(true)
    }
}
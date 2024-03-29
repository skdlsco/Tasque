package com.marahoney.tasque.ui.f_share

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

class ShareFragmentUseCase(private val fragment: Fragment) {

    fun getApplicationNameFromPackageName(packageName: String): String {

        try {
            val packageManager = fragment.context?.packageManager ?: return ""
            val info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val appName = packageManager.getApplicationLabel(info)
            return appName.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}
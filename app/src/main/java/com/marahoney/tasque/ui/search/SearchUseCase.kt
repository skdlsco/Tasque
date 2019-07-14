package com.marahoney.tasque.ui.search

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class SearchUseCase(private val activity: AppCompatActivity,
                    private val recyclerView: RecyclerView) {

    fun getApplicationNameFromPackageName(packageName: String): String {

        try {
            val packageManager = activity.packageManager
            val info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val appName = packageManager.getApplicationLabel(info)
            return appName.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}
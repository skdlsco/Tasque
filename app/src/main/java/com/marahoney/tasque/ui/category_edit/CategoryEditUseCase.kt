package com.marahoney.tasque.ui.category_edit

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class CategoryEditUseCase(private val activity: AppCompatActivity,
                          private val recyclerView: RecyclerView) {

    val intent: Intent get() = activity.intent

    fun notifyRecyclerView() {
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun notifyRecyclerView(pos: Int) {
        recyclerView.adapter?.notifyItemChanged(pos)
    }

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

    fun finishActivity() {
        activity.finish()
    }
}
package com.marahoney.tasque.ui.form_edit

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import androidx.core.content.FileProvider
import com.marahoney.tasque.BuildConfig


class FormEditUseCase(private val activity: AppCompatActivity,
                      private val recyclerView: RecyclerView) {

    val intent: Intent get() = activity.intent

    fun notifyRecyclerView() {
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun notifyRecyclerView(from: Int, to: Int) {
        recyclerView.adapter?.notifyItemMoved(from, to)
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

    fun startScreenShot(filePath: String) {
        val uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", File(filePath))
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/png")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        activity.startActivity(intent)
    }
}
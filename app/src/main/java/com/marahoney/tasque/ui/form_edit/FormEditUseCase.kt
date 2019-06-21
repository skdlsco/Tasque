package com.marahoney.tasque.ui.form_edit

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.marahoney.tasque.BuildConfig
import com.marahoney.tasque.ui.main.MainActivity
import org.jetbrains.anko.startActivity
import java.io.File


class FormEditUseCase(private val activity: AppCompatActivity,
                      private val recyclerView: RecyclerView) {

    val intent: Intent get() = activity.intent

    fun notifyRecyclerViewItemAdd(pos: Int) {
        recyclerView.adapter?.notifyItemInserted(pos)
    }

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

    fun startMainActivity() {
        activity.startActivity<MainActivity>()
    }

    fun finishActivity() {
        activity.finish()
    }
}
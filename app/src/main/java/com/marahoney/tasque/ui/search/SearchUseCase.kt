package com.marahoney.tasque.ui.search

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.marahoney.tasque.ui.category_detail.CategoryDetailActivity
import com.marahoney.tasque.ui.form_detail.FormDetailActivity
import org.jetbrains.anko.startActivity

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

    fun startCategoryDetail(token: String) {
        activity.startActivity<CategoryDetailActivity>(CategoryDetailActivity.KEY_CATEGORY_TOKEN to token)
    }

    fun startFormDetail(token: String) {
        activity.startActivity<FormDetailActivity>(FormDetailActivity.KEY_FORM_TOKEN to token)
    }

    fun finishActivity() {
        activity.finish()
    }

    fun startActivityWeb(link: String?) {
        if (link == null)
            return
        val url = if (!link.startsWith("http://") && !link.startsWith("https://"))
            "http://$link" else link
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(browserIntent)
    }

    fun startActivityApp(capturedPackage: String) {
        val pm = activity.packageManager
        val intent = pm.getLaunchIntentForPackage(capturedPackage)
        activity.startActivity(intent ?: return)
    }
}
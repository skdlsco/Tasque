package com.marahoney.tasque.ui.search

import android.content.pm.PackageManager
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
}
package com.marahoney.tasque.ui.category_detail

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.marahoney.tasque.ui.category_edit.CategoryEditActivity
import com.marahoney.tasque.ui.form_detail.FormDetailActivity
import com.marahoney.tasque.ui.menu_bottom_sheet.MenuBottomSheet
import org.jetbrains.anko.startActivity

class CategoryDetailUseCase(private val activity: AppCompatActivity,
                            private val recyclerView: RecyclerView) {

    val intent: Intent get() = activity.intent

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

    fun showMenuBottomSheet(listener: MenuBottomSheet.OnMenuClickListener) {
        val bottomSheet = MenuBottomSheet.newInstance("카테고리에 있는 폼을 수정합니다.", "카테고리를 삭제합니다.", listener)
        bottomSheet.show(activity.supportFragmentManager, "categoryBottomSheet")
    }

    fun finishActivity() {
        activity.finish()
    }

    fun startCategoryEditActivity(categoryToken: String) {
        activity.startActivity<CategoryEditActivity>(CategoryEditActivity.KEY_MODE to CategoryEditActivity.MODE_EDIT,
                CategoryEditActivity.KEY_CATEGORY_TOKEN to categoryToken)
    }

    fun notifyRecyclerView() {
        recyclerView?.adapter?.notifyDataSetChanged()
    }

    fun startFormDetailActivity(token: String?) {
        if (token != null)
            activity.startActivity<FormDetailActivity>(FormDetailActivity.KEY_FORM_TOKEN to token)
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
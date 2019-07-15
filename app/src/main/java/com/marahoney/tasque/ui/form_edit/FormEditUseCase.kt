package com.marahoney.tasque.ui.form_edit

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.marahoney.tasque.BuildConfig
import com.marahoney.tasque.ui.category_select.CategorySelectActivity
import com.marahoney.tasque.ui.dialog.DeleteDialog
import com.marahoney.tasque.ui.main.MainActivity
import com.marahoney.tasque.util.KeyboardUtil
import org.jetbrains.anko.startActivityForResult
import java.io.File


class FormEditUseCase(private val activity: AppCompatActivity,
                      private val recyclerView: RecyclerView) {

    val intent: Intent get() = activity.intent

    fun notifyRecyclerView() {
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun notifyRecyclerViewItemMove(from: Int, to: Int) {
        recyclerView.adapter?.notifyItemMoved(from, to)
    }

    fun notifyRecyclerViewItemAdd(pos: Int) {
        recyclerView.adapter?.notifyItemInserted(pos)
    }

    fun notifyRecyclerViewItemRemove(pos: Int) {
        recyclerView.adapter?.notifyItemRemoved(pos)
    }

    fun hideKeyboard() {
        KeyboardUtil.hideSoftKeyboard(activity)
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
        val intent = Intent(activity, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        activity.startActivity(intent)
    }

    fun finishActivity() {
        activity.finish()
    }

    fun startCategorySelectActivity(categoryToken: String) {
        activity.startActivityForResult<CategorySelectActivity>(FormEditActivity.REQUEST_CODE_CATEGORY_SELECT,
                CategorySelectActivity.KEY_SELECTED_CATEGORY to categoryToken)
    }

    fun showDeleteDialog(onDelete: () -> Unit){
        DeleteDialog(activity).apply {
            deleteListener = onDelete
        }.show()
    }
}
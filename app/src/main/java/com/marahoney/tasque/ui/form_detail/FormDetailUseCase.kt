package com.marahoney.tasque.ui.form_detail

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.marahoney.tasque.BuildConfig
import com.marahoney.tasque.ui.form_edit.FormEditActivity
import org.jetbrains.anko.startActivity
import java.io.File

class FormDetailUseCase(private val activity: AppCompatActivity) {

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

    fun startScreenShot(filePath: String) {
        val uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", File(filePath))
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/png")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        activity.startActivity(intent)
    }

    fun finishActivity() {
        activity.finish()
    }

    fun startEditActivity(token: String) {
        activity.startActivity<FormEditActivity>(FormEditActivity.KEY_FORM_TOKEN to token,
                FormEditActivity.KEY_MODE to FormEditActivity.MODE_EDIT)
    }
}
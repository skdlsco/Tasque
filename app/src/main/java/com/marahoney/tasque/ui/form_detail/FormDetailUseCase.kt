package com.marahoney.tasque.ui.form_detail

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.marahoney.tasque.BuildConfig
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.ui.form_edit.FormEditActivity
import com.marahoney.tasque.ui.menu_bottom_sheet.MenuBottomSheet
import com.marahoney.tasque.ui.share_bottom_sheet.FormShareBottomSheet
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

    fun showMenuBottomSheet(listener: MenuBottomSheet.OnMenuClickListener) {
        val bottomSheet = MenuBottomSheet.newInstance("폼에 저장된 내용을 수정합니다.", "폼을 삭제합니다.", listener)
        bottomSheet.show(activity.supportFragmentManager, "formMenuBottomSheet")
    }

    fun showShareBottomSheet(form: Form, onSuccess: (Uri) -> Unit ) {
        val bottomSheet = FormShareBottomSheet.newInstance(form, onSuccess)
        bottomSheet.show(activity.supportFragmentManager, "formShareBottomSheet")
    }

    fun finishActivity() {
        activity.finish()
    }

    fun startEditActivity(token: String) {
        activity.startActivity<FormEditActivity>(FormEditActivity.KEY_FORM_TOKEN to token,
                FormEditActivity.KEY_MODE to FormEditActivity.MODE_EDIT)
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
package com.marahoney.tasque.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.marahoney.tasque.ui.form_edit.FormEditActivity
import com.marahoney.tasque.ui.main.MainActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SplashUseCase(private val activity: AppCompatActivity) {

    val intent: Intent get() = activity.intent

    fun startMainActivity() {
        activity.startActivity<MainActivity>()
    }

    fun startFormEditActivity(pkgName: String, fileName: String) {
        activity.startActivity<FormEditActivity>("packageName" to pkgName, "filePath" to fileName)
    }

    fun startFormEditActivity(pkgName: String, fileName: String, link: String) {
        activity.startActivity<FormEditActivity>("packageName" to pkgName, "filePath" to fileName, "link" to link)
    }

    fun startFormEditActivity(pkgName: String, fileName: String, images: Array<String>, text: String) {
        activity.startActivity<FormEditActivity>("packageName" to pkgName, "filePath" to fileName, "image" to images, "text" to text)
    }

    fun startFormEditActivity(pkgName: String, fileName: String, images: Array<String>, text: String, link: String) {
        activity.startActivity<FormEditActivity>("packageName" to pkgName,
                "filePath" to fileName, "image" to images, "text" to text, "link" to link)
    }

    fun finish() {
        activity.finish()
    }

    fun showToast(s: String) {
        activity.toast(s)
    }
}
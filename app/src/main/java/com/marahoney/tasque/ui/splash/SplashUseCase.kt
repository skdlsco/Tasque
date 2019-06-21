package com.marahoney.tasque.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.marahoney.tasque.ui.form_edit.FormEditActivity
import com.marahoney.tasque.ui.main.MainActivity
import org.jetbrains.anko.startActivity

class SplashUseCase(private val activity: AppCompatActivity) {

    val intent: Intent get() = activity.intent

    fun startMainActivity() {
        activity.startActivity<MainActivity>()
    }

    fun finish() {
        activity.finish()
    }

    fun startFormEditActivity(pkgName: String, fileName: String) {
        activity.startActivity<FormEditActivity>("packageName" to pkgName, "filePath" to fileName)
    }
}
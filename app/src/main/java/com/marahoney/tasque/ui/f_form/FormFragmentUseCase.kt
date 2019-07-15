package com.marahoney.tasque.ui.f_form

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.marahoney.tasque.ui.form_detail.FormDetailActivity
import org.jetbrains.anko.support.v4.startActivity

class FormFragmentUseCase(private val fragment: Fragment,
                          private val recyclerView: RecyclerView) {

    fun getApplicationNameFromPackageName(packageName: String): String {

        try {
            val packageManager = fragment.context!!.packageManager
            val info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val appName = packageManager.getApplicationLabel(info)
            return appName.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun startFormDetailActivity(token: String) {
        fragment.startActivity<FormDetailActivity>(FormDetailActivity.KEY_FORM_TOKEN to token)
    }

    fun notifyRecyclerView() {
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun startActivityWeb(link: String?) {
        if (link == null)
            return
        val url = if (!link.startsWith("http://") && !link.startsWith("https://"))
            "http://$link" else link
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        fragment.activity?.startActivity(browserIntent)
    }

    fun startActivityApp(capturedPackage: String) {
        val pm = fragment.activity?.packageManager
        val intent = pm?.getLaunchIntentForPackage(capturedPackage)
        fragment.activity?.startActivity(intent ?: return)
    }
}
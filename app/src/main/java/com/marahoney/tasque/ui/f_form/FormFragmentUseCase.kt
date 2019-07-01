package com.marahoney.tasque.ui.f_form

import android.content.pm.PackageManager
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
}
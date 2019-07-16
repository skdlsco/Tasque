package com.marahoney.tasque.ui.share_bottom_sheet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import kotlinx.android.synthetic.main.layout_bottomsheet_share_form.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onLongClick
import org.jetbrains.anko.support.v4.toast
import java.net.URL


class FormShareBottomSheet() : BottomSheetDialogFragment() {

    var form: Form? = null
    var onSuccess: ((Uri) -> Unit)? = null

    constructor(form: Form, onSuccess: (Uri) -> Unit) : this() {
        this.form = form
        this.onSuccess = onSuccess
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_bottomsheet_share_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (form == null)
            return

        view.title.text = form!!.title
        view.info.text = form!!.info
        view.date.text = form!!.createText
        view.badge.imageResource = if (form!!.isWeb) R.drawable.web_badge else R.drawable.app_badge
        view.open.text = if (form!!.isWeb) form!!.link else (getApplicationNameFromPackageName(form!!.capturedPackage) + "에서 저장됨")
        view.shareLink.text = form!!.shareLink

        if (form!!.data?.none { it is FormData.Image } == true) {
            Glide.with(context!!)
                    .load(form!!.screenshot)
                    .into(view.image)
        } else {
            val url = (form!!.data?.first { it is FormData.Image }!! as FormData.Image).image
            Glide.with(context!!)
                    .load(url)
                    .into(view.image)
        }

        if (form!!.shareLink == null || form!!.shareLink.isBlank()) {
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://tasque.page.link/f/" + form!!.token))
                    .setDomainUriPrefix("https://tasque.page.link")
                    .buildShortDynamicLink()
                    .addOnSuccessListener {
                        it.shortLink
                        onSuccess?.invoke(it.shortLink)
                        val url = URL(it.shortLink.scheme, it.shortLink.host, it.shortLink.path).toString()
                        view.shareLink.text = url
                        form!!.shareLink = url
                    }

        }

        view.shareLink.onClick {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, form!!.shareLink)
                startActivity(Intent.createChooser(shareIntent, "공유하기"))
            } catch (e: Exception) {
            }
        }

        view.shareLink.onLongClick {
            val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Tasque", form!!.shareLink)
            clipboard!!.primaryClip = clip
            toast("클립보드에 저장됐습니다.")
        }
    }

    fun getApplicationNameFromPackageName(packageName: String): String {
        try {
            val packageManager = activity?.packageManager ?: return ""
            val info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val appName = packageManager.getApplicationLabel(info)
            return appName.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    companion object {
        fun newInstance() = FormShareBottomSheet()
        fun newInstance(form: Form, onSuccess: (Uri) -> Unit) = FormShareBottomSheet(form, onSuccess)
    }
}
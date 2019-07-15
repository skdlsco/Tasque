package com.marahoney.tasque.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import com.marahoney.tasque.R
import kotlinx.android.synthetic.main.dialog_delete.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class DeleteDialog : Dialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    var deleteListener: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_delete)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        deleteButton.onClick {
            deleteListener?.invoke()
            dismiss()
        }
    }
}
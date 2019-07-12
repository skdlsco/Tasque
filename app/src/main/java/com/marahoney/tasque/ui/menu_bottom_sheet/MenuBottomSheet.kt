package com.marahoney.tasque.ui.menu_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marahoney.tasque.R
import kotlinx.android.synthetic.main.layout_bottomsheet_menu.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MenuBottomSheet() : BottomSheetDialogFragment() {

    var editContent: String? = null
    var deleteContent: String? = null
    var listener: OnMenuClickListener? = null

    constructor(editContent: String, deleteContent: String, listener: OnMenuClickListener) : this() {
        this.editContent = editContent
        this.deleteContent = deleteContent
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_bottomsheet_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.editContent.text = editContent ?: ""
        view.deleteContent.text = deleteContent ?: ""

        view.editContainer.onClick {
            listener?.onEditClick()
            dismiss()
        }

        view.deleteContainer.onClick {
            listener?.onDeleteClick()
            dismiss()
        }
    }

    companion object {
        fun newInstance() = MenuBottomSheet()
        fun newInstance(editContent: String, deleteContent: String, listener: OnMenuClickListener) = MenuBottomSheet(editContent, deleteContent, listener)
    }

    interface OnMenuClickListener {
        fun onEditClick()
        fun onDeleteClick()
    }
}
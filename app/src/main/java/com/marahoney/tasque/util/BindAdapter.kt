package com.marahoney.tasque.util

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.category_detail.CategoryDetailViewModel
import com.marahoney.tasque.ui.category_edit.CategoryEditViewModel
import com.marahoney.tasque.ui.f_category.CategoryFragmentViewModel
import com.marahoney.tasque.ui.f_category.CategoryListAdapter
import com.marahoney.tasque.ui.f_form.FormFragmentViewModel
import com.marahoney.tasque.ui.f_form.FormListAdapter
import com.marahoney.tasque.ui.form_detail.FormDetailViewModel
import com.marahoney.tasque.ui.form_edit.FormDataItemTouchHelperCallback
import com.marahoney.tasque.ui.form_edit.FormDataListAdapter
import com.marahoney.tasque.ui.form_edit.FormEditViewModel


object BindAdapter {

    @JvmStatic
    @BindingAdapter(value = ["underLine"])
    fun setUnderLine(view: TextView, isUnderLine: Boolean) {
        view.paintFlags = view.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "viewModel"])
    fun setItems(view: RecyclerView, items: ArrayList<Category>, viewModel: CategoryFragmentViewModel) {

        if (view.layoutManager == null) {
            view.layoutManager = GridLayoutManager(view.context, 2)
        }
        view.adapter?.run {
            if (this is CategoryListAdapter) this.submitList(items)
        } ?: run {
            CategoryListAdapter(viewModel).run {
                view.adapter = this
                this.submitList(items)
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "viewModel", "callback"])
    fun setItems(view: RecyclerView, items: ArrayList<FormData>, viewModel: FormEditViewModel, callback: FormDataItemTouchHelperCallback) {
        view.adapter?.run {
            if (this is FormDataListAdapter) this.submitList(items)
        } ?: run {
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(view)
            FormDataListAdapter(viewModel, itemTouchHelper).run {
                view.adapter = this
                this.submitList(items)
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "viewModel"])
    fun setItems(view: RecyclerView, items: ArrayList<Form>, viewModel: FormFragmentViewModel) {

        if (view.layoutManager == null) {
            view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        view.adapter?.run {
            if (this is FormListAdapter) this.submitList(items)
        } ?: run {
            FormListAdapter(viewModel).run {
                view.adapter = this
                this.submitList(items)
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "viewModel"])
    fun setItems(view: RecyclerView, items: List<FormData>, viewModel: FormDetailViewModel) {

        if (view.layoutManager == null) {
            view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        view.adapter?.run {
            if (this is com.marahoney.tasque.ui.form_detail.FormDataListAdapter) this.submitList(items)
        } ?: run {
            com.marahoney.tasque.ui.form_detail.FormDataListAdapter(viewModel).run {
                view.adapter = this
                this.submitList(items)
            }
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["listItem", "selectedItem", "viewModel"])
    fun setItems(view: RecyclerView, items: List<Form>, selectedItem: ArrayList<String>, viewModel: CategoryEditViewModel) {

        if (view.layoutManager == null) {
            view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        view.adapter?.run {
            if (this is com.marahoney.tasque.ui.category_edit.FormListAdapter) {
                this.submitList(items)
                this.selectedItemList.clear()
                this.selectedItemList.addAll(selectedItem)
            }
        } ?: run {
            com.marahoney.tasque.ui.category_edit.FormListAdapter(viewModel).run {
                view.adapter = this
                this.submitList(items)
                this.selectedItemList.clear()
                this.selectedItemList.addAll(selectedItem)
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["listItem", "viewModel"])
    fun setItems(view: RecyclerView, items: List<Form>, viewModel: CategoryDetailViewModel) {

        if (view.layoutManager == null) {
            view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        view.adapter?.run {
            if (this is com.marahoney.tasque.ui.category_detail.FormListAdapter) {
                this.submitList(items)
            }
        } ?: run {
            com.marahoney.tasque.ui.category_detail.FormListAdapter(viewModel).run {
                view.adapter = this
                this.submitList(items)
            }
        }
    }
}
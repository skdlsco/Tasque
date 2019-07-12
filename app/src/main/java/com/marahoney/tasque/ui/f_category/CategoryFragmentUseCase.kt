package com.marahoney.tasque.ui.f_category

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.marahoney.tasque.ui.category_detail.CategoryDetailActivity
import org.jetbrains.anko.support.v4.startActivity

class CategoryFragmentUseCase(private val fragment: Fragment,
                              private val recyclerView: RecyclerView) {

    fun notifyRecyclerView() {
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun startCategoryDetail(token: String) {
        fragment.startActivity<CategoryDetailActivity>(CategoryDetailActivity.KEY_CATEGORY_TOKEN to token)
    }
}
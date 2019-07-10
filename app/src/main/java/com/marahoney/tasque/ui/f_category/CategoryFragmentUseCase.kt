package com.marahoney.tasque.ui.f_category

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class CategoryFragmentUseCase(private val fragment: Fragment,
                              private val recyclerView: RecyclerView) {

    fun notifyRecyclerView() {
        recyclerView.adapter?.notifyDataSetChanged()
    }
}
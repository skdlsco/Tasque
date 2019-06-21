package com.marahoney.tasque.ui.f_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.f_form.FormFragmentUseCase

class CategoryFragmentViewModel(private val useCase: CategoryFragmentUseCase) : BaseViewModel() {

    private val _category = MutableLiveData<ArrayList<Category>>(arrayListOf())

    val category: LiveData<ArrayList<Category>> get() = _category
}
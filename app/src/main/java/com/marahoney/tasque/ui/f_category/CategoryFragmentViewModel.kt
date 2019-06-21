package com.marahoney.tasque.ui.f_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.ui.base.BaseViewModel

class CategoryFragmentViewModel(private val useCase: CategoryFragmentUseCase) : BaseViewModel() {

    private val _category = MutableLiveData<ArrayList<Category>>(Category.dump)

    val category: LiveData<ArrayList<Category>> get() = _category
}
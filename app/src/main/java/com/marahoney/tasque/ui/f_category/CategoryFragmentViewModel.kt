package com.marahoney.tasque.ui.f_category

import androidx.lifecycle.Observer
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.base.BaseViewModel

class CategoryFragmentViewModel(private val useCase: CategoryFragmentUseCase,
                                private val dataRepository: DataRepository) : BaseViewModel() {

    val category get() = dataRepository.categories

    private val categoryObserver = Observer<ArrayList<Category>> {
        useCase.notifyRecyclerView()
    }

    init {
        category.observeForever(categoryObserver)
    }

    fun onClickForm(category: Category, pos: Int) {
        useCase.startCategoryDetail(category.token)
    }

    override fun onCleared() {
        super.onCleared()
        category.removeObserver(categoryObserver)
    }

    fun getThumbNails(token: String): List<String> {
        val category = dataRepository.categories.value?.find { it.token == token } ?: return listOf()
        val forms = dataRepository.forms.value?.filter { category.forms.contains(it.token) }
                ?: return listOf()
        val result = arrayListOf<String>()
        forms.forEach {
            if (it.data?.count { it is FormData.Image } == 0) {
                result.add(it.screenshot)
            } else {
                result.add((it.data?.filter { it is FormData.Image }?.first() as FormData.Image).image)
            }
            if (result.size >= 4)
                return@forEach
        }
        return result
    }
}
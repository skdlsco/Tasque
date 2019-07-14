package com.marahoney.tasque.ui.category_select

import android.view.MenuItem
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.base.BaseViewModel

class CategorySelectViewModel(private val useCase: CategorySelectUseCase,
                              private val dataRepository: DataRepository) : BaseViewModel() {

    val category = dataRepository.categories

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            android.R.id.home -> useCase.finishActivity()
        }
    }

    fun getThumbNails(token: String): List<String> {
        val category = dataRepository.categories.value?.find { it.token == token }
                ?: return listOf()
        val forms = dataRepository.forms.value?.filter { category.forms.contains(it.token) }
                ?: return listOf()
        val result = arrayListOf<String>()
        forms.forEach {
            if (it.data?.count { it is FormData.Image } == 0) {
                result.add(it.screenshot)
            } else {
                result.add((it.data?.first { it is FormData.Image } as FormData.Image).image)
            }
            if (result.size >= 4)
                return@forEach
        }
        return result
    }

    fun onClickForm(item: Category?, position: Int) {
        useCase.setResult(item?.token)
        useCase.finishActivity()
    }
}
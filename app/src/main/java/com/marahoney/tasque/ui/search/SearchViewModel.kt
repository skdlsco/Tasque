package com.marahoney.tasque.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.base.BaseViewModel

class SearchViewModel(private val useCase: SearchUseCase,
                      private val dataRepository: DataRepository) : BaseViewModel() {

    private val _searchQuery = MutableLiveData<String>("")
    private val _items = MutableLiveData<List<Any>>(listOf())

    val searchQuery: LiveData<String> get() = _searchQuery
    val items: LiveData<List<Any>> get() = _items

    fun onSearchQueryChanged(charSequence: CharSequence) {
        _searchQuery.value = charSequence.toString()

        if (charSequence.isNotBlank())
            _items.value = arrayListOf<Any>().apply {
                addAll(dataRepository.forms.value?.filter { it.title.contains(charSequence) }
                        ?: listOf())
                addAll(dataRepository.categories.value?.filter { it.title.contains(charSequence) }
                        ?: listOf())
            }
        else
            _items.value = listOf()
    }

    fun getApplicationNameFromPackageName(packageName: String): String {
        return useCase.getApplicationNameFromPackageName(packageName)
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

    fun onClickLink(form: Form) {
        if (form.isWeb) {
            useCase.startActivityWeb(form.link)
        } else {
            useCase.startActivityApp(form.capturedPackage)
        }
    }

    fun onClickBackButton() {
        useCase.finishActivity()
    }

    fun onClickCategory(item: Category, position: Int) {
        useCase.startCategoryDetail(item.token)
    }

    fun onClickForm(item: Form, position: Int) {
        useCase.startFormDetail(item.token)
    }
}
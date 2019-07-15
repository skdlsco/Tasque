package com.marahoney.tasque.ui.f_share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.base.BaseViewModel

class ShareFragmentViewModel(private val useCase: ShareFragmentUseCase,
                             private val dataRepository: DataRepository) : BaseViewModel() {

    private val _share = MutableLiveData<List<Any>>(listOf())
    private val _shared = MutableLiveData<List<Any>>(listOf())

    val share: LiveData<List<Any>> get() = _share
    val shared: LiveData<List<Any>> get() = _shared

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


    fun onClickForm(item: Category, position: Int) {

    }

    fun onClickForm(item: Form, position: Int) {

    }
}
package com.marahoney.tasque.ui.f_form

import androidx.lifecycle.LiveData
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.ui.base.BaseViewModel

class FormFragmentViewModel(private val useCase: FormFragmentUseCase,
                            private val dataRepository: DataRepository) : BaseViewModel() {

    val forms: LiveData<ArrayList<Form>> get() = dataRepository.forms

    init {

    }

    fun getApplicationNameFromPackageName(packageName: String): String {
        return useCase.getApplicationNameFromPackageName(packageName)
    }
}
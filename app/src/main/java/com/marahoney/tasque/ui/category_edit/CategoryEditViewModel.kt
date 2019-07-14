package com.marahoney.tasque.ui.category_edit

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.marahoney.tasque.R
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.util.TokenUtil

class CategoryEditViewModel(
        private val useCase: CategoryEditUseCase,
        private val dataRepository: DataRepository) : BaseViewModel() {

    private val _title = MutableLiveData<String>()
    private val _forms = MutableLiveData<List<Form>>()
    private val _selectedForm = MutableLiveData<ArrayList<String>>(arrayListOf())

    val title: LiveData<String> get() = _title
    val selectedForm: LiveData<ArrayList<String>> get() = _selectedForm

    val mode = useCase.intent.getIntExtra(CategoryEditActivity.KEY_MODE, 1)

    val forms: LiveData<List<Form>> get() = _forms

    private val formObserver = Observer<List<Form>> {
        _forms.value = it.filter {
            !(dataRepository.categories.value?.any { category -> category.forms.contains(it.token) }
                    ?: true) || selectedForm.value?.contains(it.token) ?: true
        }
    }

    init {
        if (mode == CategoryEditActivity.MODE_EDIT) {
            val categoryToken = useCase.intent.getStringExtra(CategoryEditActivity.KEY_CATEGORY_TOKEN)
                    ?: ""
            dataRepository.categories.value?.find { it.token == categoryToken }?.let {
                _title.value = it.title
                selectedForm.value?.addAll(it.forms)
                _selectedForm.value = _selectedForm.value
            }
        }

        dataRepository.forms.observeForever(formObserver)
    }

    fun onTitleTextChanged(text: CharSequence) {
        _title.value = text.toString()
    }

    fun getApplicationNameFromPackageName(packageName: String): String {
        return useCase.getApplicationNameFromPackageName(packageName)
    }

    fun onClickForm(item: Form?, position: Int) {
        item?.token?.let {
            if (checkItemSelected(item, position)) {
                selectedForm.value?.remove(it)
            } else {
                selectedForm.value?.add(it)
            }
        }
        _selectedForm.value = _selectedForm.value
        useCase.notifyRecyclerView(position)
    }

    fun checkItemSelected(item: Form?, position: Int): Boolean {
        return selectedForm.value?.contains(item?.token ?: return false) ?: false
    }

    private fun insertCategory() {
        if (_title.value == null || _selectedForm.value == null)
            return

        val category = Category(TokenUtil.newToken, _title.value!!, _selectedForm.value!!)
        dataRepository.insertCategory(category)
        useCase.finishActivity()
    }

    private fun updateCategory() {
        if (_title.value == null || _selectedForm.value == null)
            return
        val categoryToken = useCase.intent.getStringExtra(CategoryEditActivity.KEY_CATEGORY_TOKEN)
                ?: ""
        val category = Category(categoryToken, _title.value!!, _selectedForm.value!!)
        dataRepository.updateCategory(category)
        useCase.finishActivity()
    }

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.done -> {
                if (mode == CategoryEditActivity.MODE_CREATE)
                    insertCategory()
                else if (mode == CategoryEditActivity.MODE_EDIT)
                    updateCategory()

            }
            android.R.id.home -> {
                useCase.finishActivity()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dataRepository.forms.removeObserver(formObserver)
    }
}
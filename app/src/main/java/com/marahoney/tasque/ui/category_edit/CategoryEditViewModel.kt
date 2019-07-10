package com.marahoney.tasque.ui.category_edit

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _selectedForm = MutableLiveData<ArrayList<String>>(arrayListOf())

    val title: LiveData<String> get() = _title
    val selectedForm: LiveData<ArrayList<String>> get() = _selectedForm

    val mode = useCase.intent.getIntExtra(CategoryEditActivity.KEY_MODE, 1)

    val forms: LiveData<ArrayList<Form>> get() = dataRepository.forms

    init {
        if (mode == CategoryEditActivity.MODE_EDIT) {
            val categoryToken = useCase.intent.getStringExtra(CategoryEditActivity.KEY_FORM_TOKEN)
                    ?: ""
            dataRepository.categories.value?.find { it.token == categoryToken }?.let {
                _title.value = it.title
                selectedForm.value?.addAll(it.forms)
                _selectedForm.value = _selectedForm.value
            }
        }
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

    private fun insertForm() {
        if (_title.value == null || _selectedForm.value == null)
            return

        val category = Category(TokenUtil.newToken, _title.value!!, _selectedForm.value!!)
        dataRepository.insertCategory(category)
        useCase.finishActivity()
    }

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.done -> {
                if (mode == CategoryEditActivity.MODE_CREATE)
                    insertForm()

            }
            android.R.id.home -> {
                useCase.finishActivity()
            }
        }
    }
}
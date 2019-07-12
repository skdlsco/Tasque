package com.marahoney.tasque.ui.category_detail

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.marahoney.tasque.R
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.menu_bottom_sheet.MenuBottomSheet

class CategoryDetailViewModel(private val useCase: CategoryDetailUseCase,
                              private val dataRepository: DataRepository) : BaseViewModel() {

    private val _category = MutableLiveData<Category>()
    private val _forms = MutableLiveData<List<Form>>()

    val category: LiveData<Category> get() = _category
    val forms: LiveData<List<Form>> get() = _forms

    private val token = useCase.intent.getStringExtra(CategoryDetailActivity.KEY_CATEGORY_TOKEN)
    private val categoryObserver = Observer<List<Category>> {
        _category.value = it.find { it.token == token }
        _forms.value = dataRepository.forms.value?.filter { _category.value?.forms?.contains(it.token) == true }
        useCase.notifyRecyclerView()
    }
    private val formObserver = Observer<List<Form>> {
        _forms.value = it.filter { _category.value?.forms?.contains(it.token) == true }
        useCase.notifyRecyclerView()
    }


    init {
        dataRepository.categories.observeForever(categoryObserver)
        dataRepository.forms.observeForever(formObserver)
    }

    fun getApplicationNameFromPackageName(packageName: String): String {
        return useCase.getApplicationNameFromPackageName(packageName)
    }

    fun onClickForm(item: Form?, position: Int) {

    }

    override fun onCleared() {
        super.onCleared()
        dataRepository.categories.removeObserver(categoryObserver)
        dataRepository.forms.removeObserver(formObserver)
    }

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            android.R.id.home -> useCase.finishActivity()
            R.id.more -> useCase.showBottomSheet(object : MenuBottomSheet.OnMenuClickListener {
                override fun onEditClick() {
                    useCase.startCategoryEditActivity(token)
                }

                override fun onDeleteClick() {
                    if (_category.value != null)
                        dataRepository.removeCategory(_category.value!!)
                    useCase.finishActivity()
                }
            })
        }
    }
}
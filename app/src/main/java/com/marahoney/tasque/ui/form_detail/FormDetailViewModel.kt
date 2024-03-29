package com.marahoney.tasque.ui.form_detail

import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.marahoney.tasque.R
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.form_detail.FormDetailActivity.Companion.KEY_FORM_TOKEN
import com.marahoney.tasque.ui.menu_bottom_sheet.MenuBottomSheet
import java.net.URL


class FormDetailViewModel(private val useCase: FormDetailUseCase,
                          private val dataRepository: DataRepository) : BaseViewModel() {

    private val _form = MutableLiveData<Form>()
    private val _applicationName = MutableLiveData<String>("")
    private val _categoryTitle = MutableLiveData<String>("카테고리 지정 안됨")

    val form: LiveData<Form> get() = _form
    val applicationName: LiveData<String> get() = _applicationName
    val categoryTitle: LiveData<String> get() = _categoryTitle

    private val token = useCase.intent.getStringExtra(KEY_FORM_TOKEN)

    private val formObserver = Observer<List<Form>> {
        _form.value = it.find { it.token == token }
        _categoryTitle.value = dataRepository.categories.value?.find { category -> category.forms.contains(_form.value?.token) }?.title
                ?: "카테고리 지정 안됨"
    }

    init {
        dataRepository.forms.observeForever(formObserver)
        _applicationName.value = _form.value?.capturedPackage?.let { useCase.getApplicationNameFromPackageName(it) }
    }

    fun onClickGoScreenShot() {
        useCase.startScreenShot(_form.value?.screenshot ?: return)
    }

    fun onClickLink() {
        if (form.value == null)
            return
        if (_form.value?.isWeb == true) {
            useCase.startActivityWeb(_form.value!!.link)
        } else {
            useCase.startActivityApp(_form.value!!.capturedPackage)
        }
    }

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.share -> {
                useCase.showShareBottomSheet(form.value ?: return) {
                    val url = URL(it.scheme, it.host, it.path).toString()
                    _form.value?.shareLink = url
                    dataRepository.updateForm(_form.value ?: return@showShareBottomSheet)
                }
            }
            R.id.more -> {
                useCase.showMenuBottomSheet(object : MenuBottomSheet.OnMenuClickListener {
                    override fun onEditClick() {
                        useCase.startEditActivity(_form.value!!.token)
                    }

                    override fun onDeleteClick() {
                        if (_form.value != null) {
                            dataRepository.forms.removeObserver(formObserver)
                            dataRepository.removeForm(_form.value!!)
                        }
                        useCase.finishActivity()
                    }
                })
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

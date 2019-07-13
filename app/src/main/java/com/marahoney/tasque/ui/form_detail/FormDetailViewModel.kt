package com.marahoney.tasque.ui.form_detail

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


class FormDetailViewModel(private val useCase: FormDetailUseCase,
                          private val dataRepository: DataRepository) : BaseViewModel() {

    private val _form = MutableLiveData<Form>()
    private val _applicationName = MutableLiveData<String>("")


    val form: LiveData<Form> get() = _form
    val applicationName: LiveData<String> get() = _applicationName

    private val token = useCase.intent.getStringExtra(KEY_FORM_TOKEN)

    private val formObserver = Observer<List<Form>> {
        _form.value = it.find { it.token == token }
    }

    init {
        dataRepository.forms.observeForever(formObserver)
        _applicationName.value = _form.value?.capturedPackage?.let { useCase.getApplicationNameFromPackageName(it) }
    }

    fun onClickGoScreenShot() {
        useCase.startScreenShot(_form.value?.screenshot ?: return)
    }

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.share -> {

            }
            R.id.more -> {
                useCase.showMenuBottomSheet(object : MenuBottomSheet.OnMenuClickListener {
                    override fun onEditClick() {
                        useCase.startEditActivity(_form.value!!.token)
                    }

                    override fun onDeleteClick() {
                        if (_form.value != null){
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

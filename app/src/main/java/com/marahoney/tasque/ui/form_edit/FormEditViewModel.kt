package com.marahoney.tasque.ui.form_edit

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.R
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.splash.SplashActivity
import com.marahoney.tasque.util.TokenUtil
import java.util.*


class FormEditViewModel(private val useCase: FormEditUseCase,
                        private val dataRepository: DataRepository) : BaseViewModel() {

    private val _applicationName = MutableLiveData<String>("")
    private val _formDataArray = MutableLiveData<ArrayList<FormData>>(arrayListOf())
    private val _title = MutableLiveData<String>("")

    val applicationName: LiveData<String> get() = _applicationName
    val formDataArray: LiveData<ArrayList<FormData>> get() = _formDataArray
    val title: LiveData<String> get() = _title

    private val packageName: String = useCase.intent.getStringExtra(SplashActivity.KEY_PACKAGE_NAME)
            ?: ""
    private val filePath: String = useCase.intent.getStringExtra(SplashActivity.KEY_FILE_PATH)

    val callback = FormDataItemTouchHelperCallback { from, to ->
        if (_formDataArray.value == null)
            return@FormDataItemTouchHelperCallback
        val temp = _formDataArray.value!![from]
        _formDataArray.value!![from] = _formDataArray.value!![to]
        _formDataArray.value!![to] = temp
        _formDataArray.value = _formDataArray.value
        useCase.notifyRecyclerView(from, to)
    }

    init {
        _applicationName.value = useCase.getApplicationNameFromPackageName(packageName)
    }

    fun onTitleTextChanged(text: CharSequence) {
        _title.value = text.toString()
    }

    fun onArticleTextChanged(text: CharSequence, pos: Int) {
        val temp = _formDataArray.value!!
        (temp[pos] as FormData.Article).article = text.toString()
        _formDataArray.value = temp
    }

    fun onClickGoScreenShot() {
        useCase.startScreenShot(filePath)
    }

    fun onClickAddButton() {
        _formDataArray.value?.add(FormData.Article(""))
        _formDataArray.value = _formDataArray.value
        useCase.notifyRecyclerViewItemAdd(_formDataArray.value?.size ?: 0)
    }

    private fun insertForm() {
        if (_title.value == null || _title.value!!.isBlank())
            return

        val form = Form(TokenUtil.newToken, _title.value!!, filePath, Date(), packageName, _formDataArray.value!!.toList(), null)
        dataRepository.insertForm(form)

        useCase.startMainActivity()
        useCase.finishActivity()
    }

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.done -> {
                insertForm()
            }
            android.R.id.home -> {
                useCase.finishActivity()
            }
        }
    }
}
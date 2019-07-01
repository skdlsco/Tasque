package com.marahoney.tasque.ui.form_edit

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.R
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.form_edit.FormEditActivity.Companion.KEY_FILE_PATH
import com.marahoney.tasque.ui.form_edit.FormEditActivity.Companion.KEY_FORM_TOKEN
import com.marahoney.tasque.ui.form_edit.FormEditActivity.Companion.KEY_MODE
import com.marahoney.tasque.ui.form_edit.FormEditActivity.Companion.KEY_PACKAGE_NAME
import com.marahoney.tasque.ui.form_edit.FormEditActivity.Companion.MODE_CREATE
import com.marahoney.tasque.ui.form_edit.FormEditActivity.Companion.MODE_EDIT
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

    private lateinit var packageName: String
    private lateinit var filePath: String

    // mode 비교해서 form 데이터값 설정하기
    private val mode: Int = useCase.intent.getIntExtra(KEY_MODE, MODE_CREATE)

    val callback = FormDataItemTouchHelperCallback { from, to ->
        if (_formDataArray.value == null)
            return@FormDataItemTouchHelperCallback
        Collections.swap(_formDataArray.value!!, from, to)
        useCase.notifyRecyclerView(from, to)
    }

    init {
        if (mode == MODE_CREATE)
            initDateCreate()
        else if (mode == MODE_EDIT)
            initDateEdit()

        _applicationName.value = useCase.getApplicationNameFromPackageName(packageName)
        _formDataArray.value = _formDataArray.value?.apply { }
    }

    private fun initDateEdit() {
        val token = useCase.intent.getStringExtra(KEY_FORM_TOKEN)
        val form = dataRepository.forms.value?.find { it.token == token } ?: return // TODO: 오류
        packageName = form.capturedPackage
        filePath = form.screenshot
        _title.value = form.title
        _formDataArray.value?.addAll(form.data ?: arrayListOf())
    }

    private fun initDateCreate() {
        packageName = useCase.intent.getStringExtra(KEY_PACKAGE_NAME)
                ?: ""
        filePath = useCase.intent.getStringExtra(KEY_FILE_PATH)
        if (useCase.intent.hasExtra("image")) {
            val images = useCase.intent.getStringArrayExtra("image")
            images.forEach {
                _formDataArray.value?.add(FormData.Image("http://prometasv.com$it"))
            }
        }
        if (useCase.intent.hasExtra("text")) {
            _formDataArray.value?.add(FormData.Article(useCase.intent.getStringExtra("text")))
        }
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

        val form = Form(
                TokenUtil.newToken,
                _title.value!!,
                filePath,
                Date(),
                packageName,
                _formDataArray.value!!.toList(),
                null
        )
        dataRepository.insertForm(form)

        useCase.startMainActivity()
        useCase.finishActivity()
    }

    private fun updateForm() {
        if (_title.value == null || _title.value!!.isBlank())
            return
        val token = useCase.intent.getStringExtra(KEY_FORM_TOKEN)
        val oldForm = dataRepository.forms.value?.find { it.token == token } ?: return // TODO: 오류
        val newForm = Form(oldForm.token, _title.value!!, filePath, oldForm.createAt, oldForm.capturedPackage, _formDataArray.value)

        dataRepository.updateForm(newForm)
        useCase.finishActivity()
    }

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.done -> {
                if (mode == MODE_CREATE)
                    insertForm()
                if (mode == MODE_EDIT)
                    updateForm()
            }
            android.R.id.home -> {
                useCase.finishActivity()
            }
        }
    }
}
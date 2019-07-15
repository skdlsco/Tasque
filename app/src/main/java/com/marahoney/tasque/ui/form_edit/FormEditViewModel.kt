package com.marahoney.tasque.ui.form_edit

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.R
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.category_select.CategorySelectActivity
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
    private val _isWeb = MutableLiveData<Boolean>(false)
    private val _categoryTitle = MutableLiveData<String>("카테고리 지정하기")

    val applicationName: LiveData<String> get() = _applicationName
    val formDataArray: LiveData<ArrayList<FormData>> get() = _formDataArray
    val title: LiveData<String> get() = _title
    val isWeb: LiveData<Boolean> get() = _isWeb
    val categoryTitle: LiveData<String> get() = _categoryTitle

    var categoryToken = ""

    private lateinit var packageName: String
    private lateinit var filePath: String
    var link: String? = null

    // mode 비교해서 form 데이터값 설정하기
    private val mode: Int = useCase.intent.getIntExtra(KEY_MODE, MODE_CREATE)

    val callback = FormDataItemTouchHelperCallback { from, to ->
        if (_formDataArray.value == null)
            return@FormDataItemTouchHelperCallback
        Collections.swap(_formDataArray.value!!, from, to)
        _formDataArray.value = _formDataArray.value
        useCase.notifyRecyclerViewItemMove(from, to)
    }

    init {
        if (mode == MODE_CREATE)
            initDataCreate()
        else if (mode == MODE_EDIT)
            initDataEdit()

        _applicationName.value = useCase.getApplicationNameFromPackageName(packageName)
        _formDataArray.value = _formDataArray.value?.apply { }
    }

    private fun initDataEdit() {
        val token = useCase.intent.getStringExtra(KEY_FORM_TOKEN)
        val form = dataRepository.forms.value?.find { it.token == token } ?: return // TODO: 오류
        packageName = form.capturedPackage
        filePath = form.screenshot
        _title.value = form.title
        _formDataArray.value?.addAll(form.data ?: arrayListOf())

        _isWeb.value = form.isWeb
        link = form.link

        val category = dataRepository.categories.value?.find { it.forms.contains(token) }
        if (category != null) {
            _categoryTitle.value = category.title
            categoryToken = category.token
        }
    }

    private fun initDataCreate() {
        packageName = useCase.intent.getStringExtra(KEY_PACKAGE_NAME)
                ?: ""
        filePath = useCase.intent.getStringExtra(KEY_FILE_PATH)
        if (useCase.intent.hasExtra("image")) {
            val images = useCase.intent.getStringArrayExtra("image")
            images.forEach {
                _formDataArray.value?.add(FormData.Image("http://www.prometasv.com$it"))
            }
        }
        if (useCase.intent.hasExtra("text")) {
            _formDataArray.value?.add(FormData.Article(useCase.intent.getStringExtra("text")))
        }

        if (useCase.intent.hasExtra("link")) {
            _isWeb.value = true
            link = useCase.intent.getStringExtra("link")
        }
    }

    fun onTitleTextChanged(text: CharSequence) {
        _title.value = text.toString()
    }

    fun onArticleTextChanged(text: CharSequence, item: FormData.Article, pos: Int) {
        val realPos = _formDataArray.value?.indexOf(item) ?: pos
        val temp = _formDataArray.value!!
        if (text.isEmpty()) {
            temp.removeAt(realPos)
            _formDataArray.value = temp
            useCase.notifyRecyclerViewItemRemove(realPos)
            useCase.hideKeyboard()
        } else {
            (temp[realPos] as FormData.Article).article = text.toString()
            _formDataArray.value = temp
        }
    }

    fun onClickGoScreenShot() {
        useCase.startScreenShot(filePath)
    }

    fun onClickAddButton() {
        if (_formDataArray.value?.isNotEmpty() == true) {
            val last = _formDataArray.value?.last()
            if (last != null && last is FormData.Article && last.article.isEmpty())
                return
        }
        _formDataArray.value?.add(FormData.Article(""))
        _formDataArray.value = _formDataArray.value
        useCase.notifyRecyclerViewItemAdd(_formDataArray.value?.size ?: 0)
    }

    fun onClickCategoryButton() {
        useCase.startCategorySelectActivity(categoryToken)
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
                link
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
        val newForm = Form(oldForm.token, _title.value!!, filePath, oldForm.createAt, oldForm.capturedPackage, _formDataArray.value, link)

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

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FormEditActivity.REQUEST_CODE_CATEGORY_SELECT && resultCode == RESULT_OK && data != null) {
            val newCategoryToken = data?.getStringExtra(CategorySelectActivity.KEY_SELECTED_CATEGORY)
                    ?: ""
            if (newCategoryToken.isNotBlank()) {
                dataRepository.categories.value?.find { it.token == newCategoryToken }?.let {
                    categoryToken = it.token
                    _categoryTitle.value = it.title
                }
            }
        }
    }

    fun onClickImage(item: FormData.Image?, position: Int) {
        val realPos = _formDataArray.value?.indexOf(item ?: return) ?: position
        val temp = _formDataArray.value!!
        useCase.showDeleteDialog {
            temp.removeAt(realPos)
            _formDataArray.value = temp
            useCase.notifyRecyclerViewItemRemove(realPos)
        }
    }
}
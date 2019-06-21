package com.marahoney.tasque.ui.form_edit

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.splash.SplashActivity
import java.io.File
import java.util.*


class FormEditViewModel(private val useCase: FormEditUseCase) : BaseViewModel() {

    private val _applicationName = MutableLiveData<String>("")
    private val _formDataArray = MutableLiveData<ArrayList<FormData>>(arrayListOf())

    val applicationName: LiveData<String> get() = _applicationName
    val formDataArray: LiveData<ArrayList<FormData>> get() = _formDataArray

    private val packageName: String = useCase.intent.getStringExtra(SplashActivity.KEY_PACKAGE_NAME)
            ?: ""
    private val filePath: String = useCase.intent.getStringExtra(SplashActivity.KEY_FILE_PATH)

    val callback = FormDataItemTouchHelperCallback { from, to ->
        Collections.swap(_formDataArray.value ?: return@FormDataItemTouchHelperCallback, from, to)
        val temp = _formDataArray.value
        _formDataArray.value = temp
        useCase.notifyRecyclerView(from, to)
    }

    init {
        _applicationName.value = useCase.getApplicationNameFromPackageName(packageName)
    }

    fun onClickGoScreenShot(){
        useCase.startScreenShot(filePath)
    }

    fun onClickAddButton() {
        _formDataArray.value = _formDataArray.value?.apply {
            add(0, FormData.Article(""))
            useCase.notifyRecyclerView()
        }
    }
}
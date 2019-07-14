package com.marahoney.tasque.ui.form_edit

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.ActivityFormEditBinding
import com.marahoney.tasque.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_form_edit.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FormEditActivity : BaseActivity<ActivityFormEditBinding>() {

    override val layoutResourceId: Int = R.layout.activity_form_edit
    private val useCase by lazy { FormEditUseCase(this, recyclerView) }
    private val viewModel by viewModel<FormEditViewModel> { parametersOf(useCase) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.form_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        viewModel.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val MODE_CREATE = 1
        const val MODE_EDIT = 2
        const val KEY_MODE = "mode"
        const val KEY_FORM_TOKEN = "formToken"
        const val KEY_PACKAGE_NAME = "packageName"
        const val KEY_FILE_PATH = "filePath"
        const val REQUEST_CODE_CATEGORY_SELECT = 100
    }
}

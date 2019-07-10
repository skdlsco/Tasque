package com.marahoney.tasque.ui.category_edit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.ActivityCategoryEditBinding
import com.marahoney.tasque.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_category_edit.*
import kotlinx.android.synthetic.main.activity_category_edit.toolbar
import kotlinx.android.synthetic.main.activity_form_edit.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoryEditActivity : BaseActivity<ActivityCategoryEditBinding>() {

    override val layoutResourceId: Int = R.layout.activity_category_edit

    private val useCase by lazy { CategoryEditUseCase(this, formRecyclerView) }
    private val viewModel by viewModel<CategoryEditViewModel> { parametersOf(useCase) }

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
        menuInflater.inflate(R.menu.category_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        viewModel.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }


    companion object {
        const val MODE_CREATE = 1
        const val MODE_EDIT = 2
        const val KEY_MODE = "mode"
        const val KEY_FORM_TOKEN = "categoryToken"
    }
}

package com.marahoney.tasque.ui.category_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.ActivityCategoryDetailBinding
import com.marahoney.tasque.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_category_detail.*
import kotlinx.android.synthetic.main.activity_category_edit.*
import kotlinx.android.synthetic.main.activity_category_edit.formRecyclerView
import kotlinx.android.synthetic.main.activity_category_edit.toolbar
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoryDetailActivity : BaseActivity<ActivityCategoryDetailBinding>() {

    override val layoutResourceId: Int = R.layout.activity_category_detail
    private val useCase by lazy { CategoryDetailUseCase(this, formRecyclerView) }
    private val viewModel by viewModel<CategoryDetailViewModel> { parametersOf(useCase) }

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
        menuInflater.inflate(R.menu.category_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        viewModel.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }
    companion object {
        const val KEY_CATEGORY_TOKEN = "categoryToken"
    }
}

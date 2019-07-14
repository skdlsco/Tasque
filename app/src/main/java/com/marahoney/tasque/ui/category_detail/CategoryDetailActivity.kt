package com.marahoney.tasque.ui.category_detail

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.ActivityCategoryDetailBinding
import com.marahoney.tasque.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_category_detail.*
import kotlinx.android.synthetic.main.activity_category_edit.formRecyclerView
import kotlinx.android.synthetic.main.activity_category_edit.toolbar
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

        nestedScrollView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                nestedScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                container.minHeight = nestedScrollView.height
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            val indicator = resources.getDrawable(R.drawable.abc_ic_ab_back_material, null)
            indicator.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
            setHomeAsUpIndicator(indicator)
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

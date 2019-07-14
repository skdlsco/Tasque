package com.marahoney.tasque.ui.form_detail

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.ActivityFormDetailBinding
import com.marahoney.tasque.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_form_edit.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FormDetailActivity : BaseActivity<ActivityFormDetailBinding>() {

    override val layoutResourceId: Int = R.layout.activity_form_detail

    private val useCase by lazy { FormDetailUseCase(this) }
    private val viewModel by viewModel<FormDetailViewModel> { parametersOf(useCase) }

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
            val indicator = resources.getDrawable(R.drawable.abc_ic_ab_back_material, null)
            indicator.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
            setHomeAsUpIndicator(indicator)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.form_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        viewModel.onOptionsItemSelected(item)
        return super.onContextItemSelected(item)
    }

    companion object {
        const val KEY_FORM_TOKEN = "formToken"
    }
}

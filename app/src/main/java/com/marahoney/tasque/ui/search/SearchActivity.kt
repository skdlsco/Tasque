package com.marahoney.tasque.ui.search

import android.os.Bundle
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.ActivitySearchBinding
import com.marahoney.tasque.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_form_edit.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    override val layoutResourceId: Int = R.layout.activity_search
    private val useCase by lazy { SearchUseCase(this, recyclerView) }
    private val viewModel by viewModel<SearchViewModel> { parametersOf(useCase) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel
    }
}

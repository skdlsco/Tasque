package com.marahoney.tasque.ui.f_category


import android.os.Bundle
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.FragmentCategoryBinding
import com.marahoney.tasque.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_category

    private val useCase by lazy { CategoryFragmentUseCase(this) }
    private val viewModel by viewModel<CategoryFragmentViewModel> { parametersOf(useCase) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel
    }

    companion object {
        fun newInstance() = CategoryFragment()
    }
}

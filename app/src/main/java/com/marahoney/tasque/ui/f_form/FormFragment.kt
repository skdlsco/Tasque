package com.marahoney.tasque.ui.f_form


import android.os.Bundle
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.FragmentFormBinding
import com.marahoney.tasque.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FormFragment : BaseFragment<FragmentFormBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_form

    private val useCase by lazy { FormFragmentUseCase(this) }
    private val viewModel by viewModel<FormFragmentViewModel> { parametersOf(useCase) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel
    }

    companion object {
        fun newInstance() = FormFragment()
    }
}

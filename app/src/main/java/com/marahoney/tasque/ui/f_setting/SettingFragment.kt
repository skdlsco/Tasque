package com.marahoney.tasque.ui.f_setting

import android.os.Bundle
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.FragmentSettingBinding
import com.marahoney.tasque.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_setting

    private val useCase by lazy { SettingFragmentUseCase(this) }
    private val viewModel by viewModel<SettingFragmentViewModel> { parametersOf(useCase) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel
        viewDataBinding.lifecycleOwner = this
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }
}

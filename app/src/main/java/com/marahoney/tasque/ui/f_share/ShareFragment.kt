package com.marahoney.tasque.ui.f_share

import android.os.Bundle
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.FragmentShareBinding
import com.marahoney.tasque.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ShareFragment : BaseFragment<FragmentShareBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_share

    private val useCase by lazy { ShareFragmentUseCase(this) }
    private val viewModel by viewModel<ShareFragmentViewModel> { parametersOf(useCase) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel
        viewDataBinding.lifecycleOwner = this
    }

    companion object {

        @JvmStatic
        fun newInstance() = ShareFragment()
    }
}

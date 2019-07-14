package com.marahoney.tasque.ui.f_form


import android.os.Bundle
import android.view.ViewTreeObserver
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.FragmentFormBinding
import com.marahoney.tasque.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_form.recyclerView
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FormFragment : BaseFragment<FragmentFormBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_form

    private val useCase by lazy { FormFragmentUseCase(this, recyclerView) }
    private val viewModel by viewModel<FormFragmentViewModel> { parametersOf(useCase) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel
        viewDataBinding.lifecycleOwner = this

        nestedScrollView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                nestedScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                container.minHeight = nestedScrollView.height
            }
        })
    }

    companion object {
        fun newInstance() = FormFragment()
    }
}

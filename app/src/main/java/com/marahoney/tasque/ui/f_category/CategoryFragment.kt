package com.marahoney.tasque.ui.f_category


import android.os.Bundle
import android.view.ViewTreeObserver
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.FragmentCategoryBinding
import com.marahoney.tasque.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_category

    private val useCase by lazy { CategoryFragmentUseCase(this, recyclerView) }
    private val viewModel by viewModel<CategoryFragmentViewModel> { parametersOf(useCase) }

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
        fun newInstance() = CategoryFragment()
    }
}

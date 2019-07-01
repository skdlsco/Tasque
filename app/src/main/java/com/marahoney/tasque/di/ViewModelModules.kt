package com.marahoney.tasque.di

import com.marahoney.tasque.ui.f_category.CategoryFragmentUseCase
import com.marahoney.tasque.ui.f_category.CategoryFragmentViewModel
import com.marahoney.tasque.ui.f_form.FormFragmentUseCase
import com.marahoney.tasque.ui.f_form.FormFragmentViewModel
import com.marahoney.tasque.ui.form_detail.FormDetailUseCase
import com.marahoney.tasque.ui.form_detail.FormDetailViewModel
import com.marahoney.tasque.ui.form_edit.FormEditUseCase
import com.marahoney.tasque.ui.form_edit.FormEditViewModel
import com.marahoney.tasque.ui.main.MainUseCase
import com.marahoney.tasque.ui.main.MainViewModel
import com.marahoney.tasque.ui.splash.SplashUseCase
import com.marahoney.tasque.ui.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModules {

    val viewModelModule = module {
        viewModel { (useCase: MainUseCase) ->
            MainViewModel(useCase, get())
        }

        viewModel { (useCase: SplashUseCase) ->
            SplashViewModel(useCase, get(), get())
        }

        viewModel { (useCase: CategoryFragmentUseCase) ->
            CategoryFragmentViewModel(useCase)
        }

        viewModel { (useCase: FormEditUseCase) ->
            FormEditViewModel(useCase, get())
        }

        viewModel { (useCase: FormFragmentUseCase) ->
            FormFragmentViewModel(useCase, get())
        }

        viewModel { (useCase: FormDetailUseCase) ->
            FormDetailViewModel(useCase, get())
        }
    }
}
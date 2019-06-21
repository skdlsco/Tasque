package com.marahoney.tasque.di

import com.marahoney.tasque.ui.f_category.CategoryFragmentUseCase
import com.marahoney.tasque.ui.f_category.CategoryFragmentViewModel
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
            MainViewModel(useCase)
        }

        viewModel { (useCase: SplashUseCase) ->
            SplashViewModel(useCase)
        }

        viewModel { (useCase: CategoryFragmentUseCase) ->
            CategoryFragmentViewModel(useCase)
        }

        viewModel { (useCase: FormEditUseCase) ->
            FormEditViewModel(useCase)
        }
    }
}
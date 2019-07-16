package com.marahoney.tasque.di

import com.marahoney.tasque.ui.category_detail.CategoryDetailUseCase
import com.marahoney.tasque.ui.category_detail.CategoryDetailViewModel
import com.marahoney.tasque.ui.category_edit.CategoryEditUseCase
import com.marahoney.tasque.ui.category_edit.CategoryEditViewModel
import com.marahoney.tasque.ui.category_select.CategorySelectUseCase
import com.marahoney.tasque.ui.category_select.CategorySelectViewModel
import com.marahoney.tasque.ui.f_category.CategoryFragmentUseCase
import com.marahoney.tasque.ui.f_category.CategoryFragmentViewModel
import com.marahoney.tasque.ui.f_form.FormFragmentUseCase
import com.marahoney.tasque.ui.f_form.FormFragmentViewModel
import com.marahoney.tasque.ui.f_setting.SettingFragmentUseCase
import com.marahoney.tasque.ui.f_setting.SettingFragmentViewModel
import com.marahoney.tasque.ui.f_share.ShareFragmentUseCase
import com.marahoney.tasque.ui.f_share.ShareFragmentViewModel
import com.marahoney.tasque.ui.form_detail.FormDetailUseCase
import com.marahoney.tasque.ui.form_detail.FormDetailViewModel
import com.marahoney.tasque.ui.form_edit.FormEditUseCase
import com.marahoney.tasque.ui.form_edit.FormEditViewModel
import com.marahoney.tasque.ui.main.MainUseCase
import com.marahoney.tasque.ui.main.MainViewModel
import com.marahoney.tasque.ui.search.SearchUseCase
import com.marahoney.tasque.ui.search.SearchViewModel
import com.marahoney.tasque.ui.splash.SplashUseCase
import com.marahoney.tasque.ui.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModules {

    val viewModelModule = module {
        viewModel { (useCase: MainUseCase) ->
            MainViewModel(useCase, get(), get())
        }

        viewModel { (useCase: SplashUseCase) ->
            SplashViewModel(useCase, get(), get())
        }

        viewModel { (useCase: CategoryFragmentUseCase) ->
            CategoryFragmentViewModel(useCase, get())
        }

        viewModel { (useCase: CategoryEditUseCase) ->
            CategoryEditViewModel(useCase, get())
        }

        viewModel { (useCase: CategoryDetailUseCase) ->
            CategoryDetailViewModel(useCase, get())
        }

        viewModel { (useCase: CategorySelectUseCase) ->
            CategorySelectViewModel(useCase, get())
        }

        viewModel { (useCase: FormEditUseCase) ->
            FormEditViewModel(useCase, get(), get())
        }

        viewModel { (useCase: FormFragmentUseCase) ->
            FormFragmentViewModel(useCase, get())
        }

        viewModel { (useCase: FormDetailUseCase) ->
            FormDetailViewModel(useCase, get())
        }

        viewModel { (useCase: SearchUseCase) ->
            SearchViewModel(useCase, get())
        }

        viewModel { (useCase: ShareFragmentUseCase) ->
            ShareFragmentViewModel(useCase, get())
        }

        viewModel { (useCase: SettingFragmentUseCase) ->
            SettingFragmentViewModel(useCase, get())
        }
    }
}
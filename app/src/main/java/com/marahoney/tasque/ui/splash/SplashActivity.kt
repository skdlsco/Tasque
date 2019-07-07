package com.marahoney.tasque.ui.splash

import android.os.Bundle
import com.marahoney.tasque.R
import com.marahoney.tasque.databinding.ActivitySplashBinding
import com.marahoney.tasque.ui.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val layoutResourceId: Int = R.layout.activity_splash
    private val useCase by lazy { SplashUseCase(this) }
    private val viewModel by viewModel<SplashViewModel> { parametersOf(useCase) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel
    }

    companion object {
        const val MODE_CREATE_FORM = "MODE_CREATE_FORM"
        const val KEY_PACKAGE_NAME = "packageName"
        const val KEY_FILE_PATH = "filePath"
        const val KEY_WEB_LINK = "webLink"
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        fromApp?.onActivityResult(requestCode, resultCode, data)
//    }
}

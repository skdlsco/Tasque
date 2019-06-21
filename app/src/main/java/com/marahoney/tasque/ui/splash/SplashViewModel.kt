package com.marahoney.tasque.ui.splash

import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.splash.SplashActivity.Companion.KEY_FILE_PATH
import com.marahoney.tasque.ui.splash.SplashActivity.Companion.KEY_PACKAGE_NAME

class SplashViewModel(private val useCase: SplashUseCase,
                      private val dataRepository: DataRepository) : BaseViewModel() {

    val mode = if (useCase.intent.hasExtra("mode"))
        useCase.intent.getStringExtra("mode") == SplashActivity.MODE_CREATE_FORM
    else
        false

    init {
        if (!dataRepository.isLoaded)
            dataRepository.loadData()

        if (mode) {
            val packageName = useCase.intent.getStringExtra(KEY_PACKAGE_NAME)
            val filePath = useCase.intent.getStringExtra(KEY_FILE_PATH)
            useCase.startFormEditActivity(packageName, filePath)
            useCase.finish()
        } else
            Thread {
                Thread.sleep(2000)
                useCase.startMainActivity()
                useCase.finish()
            }.start()

    }
}
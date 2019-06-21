package com.marahoney.tasque.ui.splash

import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.remote.NetworkRepository
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.splash.SplashActivity.Companion.KEY_FILE_PATH
import com.marahoney.tasque.ui.splash.SplashActivity.Companion.KEY_PACKAGE_NAME
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SplashViewModel(private val useCase: SplashUseCase,
                      private val dataRepository: DataRepository,
                      private val networkRepository: NetworkRepository) : BaseViewModel() {

    val mode = if (useCase.intent.hasExtra("mode"))
        useCase.intent.getStringExtra("mode") == SplashActivity.MODE_CREATE_FORM
    else
        false

    init {
        if (!dataRepository.isLoaded)
            dataRepository.loadData()

        if (mode) {

            postPicture()
//            useCase.startFormEditActivity(packageName, filePath)
//            useCase.finish()
        } else
            Thread {
                Thread.sleep(2000)
                useCase.startMainActivity()
                useCase.finish()
            }.start()
    }

    private fun postPicture() {

        val path = useCase.intent.getStringExtra(KEY_FILE_PATH)
        val screenshot = File(path)
        val body = RequestBody.create(MediaType.parse("image/*"), screenshot)
        val part = MultipartBody.Part.createFormData("image", screenshot.name, body)

        networkRepository.postPicture(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    val packageName = useCase.intent.getStringExtra(KEY_PACKAGE_NAME)
                    val filePath = useCase.intent.getStringExtra(KEY_FILE_PATH)
                    useCase.startFormEditActivity(packageName, filePath, it.images.toTypedArray(), it.text)
                }, {
                    it.printStackTrace()
                    useCase.showToast("서버 에러 발생")
                    useCase.finish()
                })
                .also { addDisposable(it) }
    }
}
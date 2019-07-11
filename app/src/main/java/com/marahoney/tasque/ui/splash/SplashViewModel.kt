package com.marahoney.tasque.ui.splash

import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.remote.NetworkRepository
import com.marahoney.tasque.ui.base.BaseViewModel
import com.marahoney.tasque.ui.form_edit.FormEditActivity
import com.marahoney.tasque.ui.splash.SplashActivity.Companion.KEY_FILE_PATH
import com.marahoney.tasque.ui.splash.SplashActivity.Companion.KEY_WEB_LINK
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

    var splashRunnable: SplashThread? = SplashThread()

    init {
        if (!dataRepository.isLoaded)
            dataRepository.loadData()

        if (mode) {
            postPicture()
        } else {
            Thread(splashRunnable).start()
            splashRunnable?.callback = {
                useCase.startMainActivity()
                useCase.finish()
            }
        }
    }

    private fun postPicture() {

        val path = useCase.intent.getStringExtra(KEY_FILE_PATH)
        val screenshot = File(path)
        val body = RequestBody.create(MediaType.parse("image/*"), screenshot)
        val part = MultipartBody.Part.createFormData("image", screenshot.name, body)

        val link = useCase.intent.getStringExtra(KEY_WEB_LINK) ?: ""

        networkRepository.postPicture(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val packageName = useCase.intent.getStringExtra(FormEditActivity.KEY_PACKAGE_NAME)
                    val filePath = useCase.intent.getStringExtra(FormEditActivity.KEY_FILE_PATH)
                    if (link.isNotBlank())
                        useCase.startFormEditActivity(packageName, filePath, it.images.toTypedArray(), it.text, link)
                    else
                        useCase.startFormEditActivity(packageName, filePath, it.images.toTypedArray(), it.text)

                    useCase.finish()
                }, {
                    it.printStackTrace()
                    useCase.showToast("서버 에러 발생")
                    val packageName = useCase.intent.getStringExtra(FormEditActivity.KEY_PACKAGE_NAME)
                    val filePath = useCase.intent.getStringExtra(FormEditActivity.KEY_FILE_PATH)
                    if (link.isNotBlank())
                        useCase.startFormEditActivity(packageName, filePath, link)
                    else
                        useCase.startFormEditActivity(packageName, filePath)

                    useCase.finish()
                })
                .also { addDisposable(it) }
    }

    override fun onCleared() {
        super.onCleared()
        splashRunnable?.callback = null
        splashRunnable = null
    }
}
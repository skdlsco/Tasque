package com.marahoney.tasque.ui.main

import android.util.Log
import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.remote.NetworkRepository
import com.marahoney.tasque.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val useCase: MainUseCase,
                    private val dataRepository: DataRepository,
                    private val networkRepository: NetworkRepository) : BaseViewModel() {

    init {
        if (!dataRepository.isLoaded)
            dataRepository.loadData()

        if (dataRepository.userId.value == null) {
            if (useCase.checkPermission()) {
                login()
            }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (dataRepository.userId.value == null && useCase.checkPermission()) {
            login()
        }
    }

    private fun login() {
        networkRepository.postLogin(useCase.getImei())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("asdf", "asdsf ${it.id}")
                    dataRepository.saveUserId(it.id)
                }, {
                    Log.e("Asdf", "${it.message}")
                })
                .also { addDisposable(it) }
    }
}
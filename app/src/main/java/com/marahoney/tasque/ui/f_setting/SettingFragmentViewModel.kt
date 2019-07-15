package com.marahoney.tasque.ui.f_setting

import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.ui.base.BaseViewModel

class SettingFragmentViewModel(private val useCase: SettingFragmentUseCase,
                               private val dataRepository: DataRepository) : BaseViewModel() {

    val isShow = dataRepository.isShow

    fun changeDisplayShow(isShow: Boolean) {
        dataRepository.changeIsShow(isShow)
        if (isShow)
            useCase.startService()
        else
            useCase.stopService()
    }
}
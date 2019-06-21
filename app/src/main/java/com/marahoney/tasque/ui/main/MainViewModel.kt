package com.marahoney.tasque.ui.main

import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.ui.base.BaseViewModel

class MainViewModel(private val useCase: MainUseCase,
                    private val dataRepository: DataRepository) : BaseViewModel() {

    init {
        if (!dataRepository.isLoaded)
            dataRepository.loadData()
    }
}
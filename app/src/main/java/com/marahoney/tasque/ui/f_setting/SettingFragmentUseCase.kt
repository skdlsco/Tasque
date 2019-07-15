package com.marahoney.tasque.ui.f_setting

import androidx.fragment.app.Fragment
import com.marahoney.tasque.ui.main.MainActivity

class SettingFragmentUseCase(private val fragment: Fragment) {

    fun startService() {
        if (fragment.activity is MainActivity) {
            (fragment.activity as MainActivity).startCaptureService()
        }
    }

    fun stopService() {
        if (fragment.activity is MainActivity) {
            (fragment.activity as MainActivity).stopCaptureService()
        }
    }
}
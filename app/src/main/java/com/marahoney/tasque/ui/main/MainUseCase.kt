package com.marahoney.tasque.ui.main

import android.Manifest
import android.content.Context
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

class MainUseCase(private val activity: AppCompatActivity) {

    fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PERMISSION_GRANTED
    }

    fun getImei(): String {
        if (!checkPermission()) return ""
        val tm = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceId
    }
}
package com.marahoney.tasque.capture

import android.app.Activity
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.marahoney.tasque.ui.splash.SplashActivity
import com.marahoney.tasque.util.Utils.getAppNameFromPkgName
import java.util.*


class MyReceiver : BroadcastReceiver() {

    var oldTime = 0L

    var lastApplicationPackageName = ""

    override fun onReceive(context: Context, intent: Intent) {
//        val action = intent.action
//        if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
//            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
//
//            val am = context
//                    .getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
//            val packageName = am.getRunningTasks(1)[0].topActivity
//                    .packageName
//            if (reason != null && reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
//                Log.e("onReceive", ">>>>> Home Button Click")
//
//                val currentAppName = getAppNameFromPkgName(context, lastApplicationPackageName)
//                if (currentAppName == "")
//                    return
//
//
//                val newTime = Date().time
//                oldTime = if (newTime - oldTime < 1000L) {
//                    SplashActivity.screenCapture?.capture {
//                        val intent = Intent(context, SplashActivity::class.java).apply {
//                            putExtra("mode", SplashActivity.MODE_CREATE_FORM)
//                            putExtra("packageName", lastApplicationPackageName)
//                            putExtra("imageFile", it)
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        }
//                        context.startActivity(intent)
//                    }
//                    0L
//                } else newTime
//            }
//            lastApplicationPackageName = packageName
//        }
    }


    companion object {
        const val SYSTEM_DIALOG_REASON_KEY = "reason"
        const val SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions"
        const val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
        const val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"

    }
}

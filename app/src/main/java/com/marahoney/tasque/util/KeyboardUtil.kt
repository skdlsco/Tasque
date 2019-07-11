package com.marahoney.tasque.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.window.decorView.windowToken, 0)
    }
}

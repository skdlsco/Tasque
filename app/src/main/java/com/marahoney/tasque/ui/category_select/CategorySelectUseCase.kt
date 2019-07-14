package com.marahoney.tasque.ui.category_select

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class CategorySelectUseCase(private val activity: AppCompatActivity) {

    fun finishActivity() {
        activity.finish()
    }

    fun setResult(token: String?) {
        activity.setResult(RESULT_OK, Intent().apply { putExtra(CategorySelectActivity.KEY_SELECTED_CATEGORY, token) })
    }
}
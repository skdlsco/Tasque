package com.marahoney.tasque.data.local

import androidx.lifecycle.LiveData
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form

interface DataRepository {

    var isLoaded: Boolean

    val isShow: LiveData<Boolean>
    val userId: LiveData<String>

    val forms: LiveData<ArrayList<Form>>
    val categories: LiveData<ArrayList<Category>>

    fun changeIsShow(isShow: Boolean)
    fun saveUserId(userId: String)

    fun insertForm(form: Form)
    fun updateForm(form: Form)
    fun removeForm(form: Form)

    fun insertCategory(category: Category)
    fun updateCategory(category: Category)
    fun removeCategory(category: Category)

    fun loadData()
}
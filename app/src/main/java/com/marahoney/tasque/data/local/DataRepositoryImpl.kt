package com.marahoney.tasque.data.local

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.util.ArrayListLiveData
import com.marahoney.tasque.util.CustomGsonBuilder

class DataRepositoryImpl(private val context: Context) : DataRepository {

    override var isLoaded: Boolean = false

    private val gson = CustomGsonBuilder.getGsonBuilder()

    private val userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val userEdit = userPref.edit()

    private val showPref = context.getSharedPreferences("show", Context.MODE_PRIVATE)
    private val showEdit = showPref.edit()

    private val formPref = context.getSharedPreferences("form", Context.MODE_PRIVATE)
    private val formEdit = formPref.edit()

    private val categoryPref = context.getSharedPreferences("category", Context.MODE_PRIVATE)
    private val categoryEdit = categoryPref.edit()

    private var _forms = ArrayListLiveData<Form>()
    private var _categories = ArrayListLiveData<Category>()
    private var _isShow = MutableLiveData<Boolean>()
    private var _userId = MutableLiveData<String>()
    override val forms: LiveData<ArrayList<Form>> get() = _forms
    override val categories: LiveData<ArrayList<Category>> get() = _categories
    override val isShow: LiveData<Boolean> get() = _isShow
    override val userId: LiveData<String> get() = _userId

    override fun saveUserId(userId: String) {
        userEdit.putString("userId", userId)
        userEdit.commit()
        _userId.value = userId
    }

    override fun changeIsShow(isShow: Boolean) {
        showEdit.putBoolean("isShow", isShow)
        showEdit.commit()
        _isShow.value = isShow
    }

    override fun insertForm(form: Form) {
        val json = gson.toJson(form)
        formEdit.putString(form.token, json)
        formEdit.commit()
        _forms.add(form)
    }

    override fun updateForm(form: Form) {
        val json = gson.toJson(form)
        formEdit.putString(form.token, json)
        formEdit.commit()
        val i = _forms.value?.indexOfFirst { it.token == form.token } ?: -1
        if (i != -1)
            _forms.value?.set(i, form)
        _forms.notifyDataChanged()
    }

    override fun removeForm(form: Form) {
        formEdit.remove(form.token)
        formEdit.commit()
        _categories.value?.forEachIndexed { index, category ->
            if (category.forms.contains(form.token)) {
                val new = category.apply {
                    forms = ArrayList<String>(category.forms).apply { remove(form.token) }.toList()
                }
                updateCategory(new)
            }
        }
        _forms.remove(form)
        _forms.notifyDataChanged()
    }

    override fun insertCategory(category: Category) {
        val json = gson.toJson(category)
        categoryEdit.putString(category.token, json)
        categoryEdit.commit()
        _categories.add(category)
    }

    override fun updateCategory(category: Category) {
        val json = gson.toJson(category)
        categoryEdit.putString(category.token, json)
        categoryEdit.commit()
        val i = _categories.value?.indexOfFirst { it.token == category.token } ?: -1
        if (i != -1)
            _categories.value?.set(i, category)
        _categories.notifyDataChanged()
    }

    override fun removeCategory(category: Category) {
        categoryEdit.remove(category.token)
        categoryEdit.commit()
        _categories.remove(category)
        _categories.notifyDataChanged()
    }

    override fun loadData() {
        _forms.value = loadForm()
        _categories.value = loadCategory()
        _isShow.value = showPref.getBoolean("isShow", true)
        _userId.value = userPref.getString("userId", null)
    }

    private fun loadForm(): ArrayList<Form> {
        val result = arrayListOf<Form>()
        formPref.all.values.forEach {
            if (it != null && it is String) {
                val form = gson.fromJson<Form>(it, Form::class.java)
                result.add(form)
            }
        }
        result.sortBy { it.createAt }
        result.reverse()
        return result
    }

    private fun loadCategory(): ArrayList<Category> {
        val result = arrayListOf<Category>()
        categoryPref.all.values.forEach {
            if (it != null && it is String) {
                val category = gson.fromJson<Category>(it, Category::class.java)
                result.add(category)
            }
        }
        return result
    }

}
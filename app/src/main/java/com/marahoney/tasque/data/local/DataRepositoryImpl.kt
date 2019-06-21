package com.marahoney.tasque.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.util.ArrayListLiveData
import com.marahoney.tasque.util.CustomGsonBuilder

class DataRepositoryImpl(private val context: Context) : DataRepository {

    override var isLoaded: Boolean = false

    private val gson = CustomGsonBuilder.getGsonBuilder()

    private val formPref = context.getSharedPreferences("form", Context.MODE_PRIVATE)
    private val formEdit = formPref.edit()

    private val categoryPref = context.getSharedPreferences("category", Context.MODE_PRIVATE)
    private val categoryEdit = categoryPref.edit()

    private var _forms = ArrayListLiveData<Form>()
    private var _categories = ArrayListLiveData<Category>()

    override val forms: LiveData<ArrayList<Form>> get() = _forms
    override val categories: LiveData<ArrayList<Category>> get() = _categories

    override fun insertForm(form: Form) {
        val json = gson.toJson(form)
        formEdit.putString(form.token, json)
        formEdit.commit()
    }

    override fun updateForm(form: Form) {
        val json = gson.toJson(form)
        formEdit.putString(form.token, json)
        formEdit.commit()
    }

    override fun removeForm(form: Form) {
        formEdit.remove(form.token)
        formEdit.commit()
    }

    override fun insertCategory(category: Category) {
        val json = gson.toJson(category)
        categoryEdit.putString(category.token, json)
        categoryEdit.commit()
    }

    override fun updateCategory(category: Category) {
        val json = gson.toJson(category)
        categoryEdit.putString(category.token, json)
        categoryEdit.commit()
    }

    override fun removeCategory(category: Category) {
        categoryEdit.remove(category.token)
        categoryEdit.commit()
    }

    override fun loadData() {
        _forms.value = loadForm()
        _categories.value = loadCategory()
    }

    private fun loadForm(): ArrayList<Form> {
        val result = arrayListOf<Form>()
        formPref.all.values.forEach {
            if (it != null && it is String) {
                val form = gson.fromJson<Form>(it, Form::class.java)
                result.add(form)
            }
        }
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
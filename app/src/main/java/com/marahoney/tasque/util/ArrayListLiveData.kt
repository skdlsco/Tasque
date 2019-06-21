/*
 * Created by 나인채 on 5/26/2019
 * Copyright (c) 2019. All rights reversed
 */

package com.marahoney.tasque.util

import androidx.lifecycle.MutableLiveData
import java.util.*

class ArrayListLiveData<T>(value: ArrayList<T>? = null) : MutableLiveData<ArrayList<T>>(value
        ?: arrayListOf()) {

    fun swap(from: Int, to: Int) {
        value = value?.apply { Collections.swap(value, from, to) }
    }

    fun add(item: T) {
        value = value?.apply { add(item) }
    }

    fun addAll(list: List<T>) {
        value = value?.apply { addAll(list) }
    }

    fun clear() {
        value = value?.apply { clear() }
    }

    fun remove(item: T) {
        value = value?.apply { remove(item) }
    }

    fun notifyDataChanged() {
        val items = value
        value = items
    }
}

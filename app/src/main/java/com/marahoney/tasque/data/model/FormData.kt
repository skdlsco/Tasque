package com.marahoney.tasque.data.model

import androidx.recyclerview.widget.DiffUtil

sealed class FormData {
    class Image(var image: String) : FormData()
    class Article(var article: String) : FormData()

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<FormData>() {
            override fun areItemsTheSame(oldItem: FormData, newItem: FormData): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: FormData, newItem: FormData): Boolean = oldItem == newItem
        }
    }
}
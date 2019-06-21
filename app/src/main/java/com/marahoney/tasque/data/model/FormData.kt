package com.marahoney.tasque.data.model

import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

@Keep
sealed class FormData(val mode: String) {

    @Keep
    class Image(var image: String = "") : FormData("image"), Serializable
    @Keep
    class Article(var article: String = "") : FormData("article"), Serializable

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<FormData>() {
            override fun areItemsTheSame(oldItem: FormData, newItem: FormData): Boolean =
                    if (oldItem is Article && newItem is Article) {
                        oldItem.article == newItem.article
                    } else if (oldItem is Image && newItem is Image) {
                        oldItem.image == newItem.image
                    } else oldItem == newItem

            override fun areContentsTheSame(oldItem: FormData, newItem: FormData): Boolean =
                    if (oldItem is Article && newItem is Article) {
                        oldItem == newItem
                    } else if (oldItem is Image && newItem is Image) {
                        oldItem == newItem
                    } else oldItem == newItem
        }
    }
}
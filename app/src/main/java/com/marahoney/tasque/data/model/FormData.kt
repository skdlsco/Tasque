package com.marahoney.tasque.data.model

import android.util.Log
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import com.marahoney.tasque.util.TokenUtil
import java.io.Serializable

@Keep
sealed class FormData(val mode: String, val token: String = TokenUtil.newToken) {

    @Keep
    class Image(var image: String = "") : FormData("image"), Serializable

    @Keep
    class Article(var article: String = "") : FormData("article"), Serializable

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<FormData>() {
            override fun areItemsTheSame(oldItem: FormData, newItem: FormData): Boolean {
                return if (oldItem is Article && newItem is Article) {
                    oldItem.token == newItem.token
                } else if (oldItem is Image && newItem is Image) {
                    oldItem.image == newItem.image
                } else false

            }

            override fun areContentsTheSame(oldItem: FormData, newItem: FormData): Boolean {
                return if (oldItem is Article && newItem is Article) {
                    oldItem.article == newItem.article
                } else if (oldItem is Image && newItem is Image) {
                    oldItem.image == newItem.image
                } else false
            }

        }
    }
}
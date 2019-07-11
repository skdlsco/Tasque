package com.marahoney.tasque.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Category(@SerializedName("token")
                    var token: String,
                    @SerializedName("title")
                    var title: String,
                    @SerializedName("forms")
                    var forms: List<String>,
                    @SerializedName("date")
                    var createAt: Date = Date()) : Serializable {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem == newItem
        }
    }
}
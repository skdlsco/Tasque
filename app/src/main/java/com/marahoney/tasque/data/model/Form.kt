package com.marahoney.tasque.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Form(
        @SerializedName("token")
        val token: String,
        @SerializedName("title")
        var title: String,
        @SerializedName("screenshot")
        val screenshot: String,
        @SerializedName("date")
        var createAt: Date,
        @SerializedName("capturedPackage")
        val capturedPackage: String,
        @SerializedName("data")
        var data: List<FormData>? = listOf(),
        @SerializedName("link")
        val link: String? = null): Serializable {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Form>() {

            override fun areItemsTheSame(oldItem: Form, newItem: Form): Boolean {
                return oldItem.token == newItem.token
            }

            override fun areContentsTheSame(oldItem: Form, newItem: Form): Boolean {
                return oldItem == newItem
            }
        }
    }
}
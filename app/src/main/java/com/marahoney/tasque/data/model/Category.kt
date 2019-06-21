package com.marahoney.tasque.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class Category(@SerializedName("title")
                    val title: String,
                    @SerializedName("forms")
                    val forms: List<Form>) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem == newItem
        }

        val dump =
                arrayListOf(Category("title1",
                        listOf(Form("form1", "",
                                listOf(FormData.Image(""), FormData.Article("asdfs"), FormData.Image(""))
                        ), Form("form2", "",
                                listOf(FormData.Image(""), FormData.Article("asdfs"), FormData.Image(""))
                        ), Form("form3", "",
                                listOf(FormData.Image(""), FormData.Article("asdfs"), FormData.Image(""))
                        ), Form("form4", "",
                                listOf(FormData.Image(""), FormData.Article("asdfs"), FormData.Image(""))
                        ))
                ))
    }
}
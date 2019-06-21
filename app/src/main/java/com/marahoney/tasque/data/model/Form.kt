package com.marahoney.tasque.data.model

import com.google.gson.annotations.SerializedName

data class Form(
        @SerializedName("title")
        val title: String,
        @SerializedName("screenshot")
        val screenshot: String,
        @SerializedName("data")
        val data: List<FormData>)
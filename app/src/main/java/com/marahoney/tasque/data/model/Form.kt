package com.marahoney.tasque.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
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
        val link: String? = null,
        @SerializedName("share")
        var share: Boolean = false,
        @SerializedName("shareLink")
        var shareLink: String = "") : Serializable {

    val isWeb: Boolean get() = link != null

    val info: String
        get() {
            val result = StringBuilder()
            val pictureSize = data?.filter { it is FormData.Image }?.size ?: 0

            if (pictureSize == 1)
                result.append("사진 1장")
            else if (pictureSize > 1)
                result.append("${pictureSize}장의 사진")

            val letterCount = data?.filter { it is FormData.Article }?.sumBy { (it as FormData.Article).article.length }
                    ?: 0
            if (result.isNotBlank())
                result.append(", ")

            result.append("글자 수 ${letterCount}자")
            return result.toString()
        }

    val createText: String get() = simpleDateFormat.format(createAt)

    companion object {
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd(E)에 생성됨.", Locale.KOREAN)

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
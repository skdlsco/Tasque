package com.marahoney.tasque.util

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.marahoney.tasque.data.model.FormData
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object CustomGsonBuilder {
    fun getCustomConverter(): Converter.Factory {
        return GsonConverterFactory.create(getGsonBuilder())
    }


    fun getGsonBuilder(): Gson {
        val builder = GsonBuilder()


        builder.registerTypeAdapter(object : TypeToken<FormData>() {}.type, JsonDeserializer { json, _, _ ->
            if (json == null || !json.isJsonObject)
                return@JsonDeserializer FormData.Article("")

            return@JsonDeserializer try {
                val jo = json.asJsonObject
                val mode = jo["mode"].asString
                if (mode == "image")
                    FormData.Image(jo["image"].asString)
                else
                    FormData.Article(jo["article"].asString)
            } catch (e: Exception) {
                e.printStackTrace()
                FormData.Article("")
            }
        })

        builder.registerTypeAdapter(object : TypeToken<FormData>() {}.type, JsonSerializer<FormData>
        { src: FormData?, typeOfSrc: Type?, context: JsonSerializationContext? ->
            val jo = JsonObject()
            jo.addProperty("mode", src?.mode)
            if (src is FormData.Article) {
                jo.addProperty("article", src.article)
            } else if (src is FormData.Image) {
                jo.addProperty("image", src.image)
            }
            jo
        })

        return builder.create()
    }

    private fun JsonObject.opt(key: String): String {
        return if (!this.has(key) || this[key].isJsonNull) {
            ""
        } else {
            this[key].asString
        }
    }
}

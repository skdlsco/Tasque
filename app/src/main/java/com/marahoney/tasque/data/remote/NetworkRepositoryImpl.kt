package com.marahoney.tasque.data.remote

import android.util.Log
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.data.model.ServerForm
import com.marahoney.tasque.data.model.ServerFormData
import com.marahoney.tasque.data.remote.response.EmptyResponse
import com.marahoney.tasque.data.remote.response.FormCreateResponse
import com.marahoney.tasque.data.remote.response.PostLoginResponse
import com.marahoney.tasque.data.remote.response.PostPictureResponse
import io.reactivex.Single
import okhttp3.MultipartBody

class NetworkRepositoryImpl(private val networkApi: NetworkApi) : NetworkRepository {

    override fun postPicture(picture: MultipartBody.Part): Single<PostPictureResponse> {
        return networkApi.postPicture(picture)
    }

    override fun postLogin(imei: String): Single<PostLoginResponse> {
        return networkApi.postLogin(imei)
    }

    override fun formCreate(userId: String, form: Form): Single<FormCreateResponse> {
        val serverForm = ServerForm(false, form.title, form.data!!.map {
            when (it) {
                is FormData.Article -> ServerFormData("text", it.article)
                is FormData.Image -> ServerFormData("image", it.image)
            }
        }, if (form.isWeb) form.link!! else form.capturedPackage)

        return networkApi.formCreate(userId, serverForm)
    }

    override fun formUpdate(userId: String, formId: String, form: Form): Single<EmptyResponse> {

        Log.e("asdf", "user = $userId")
        Log.e("asdf", "form = $formId")
        val serverForm = ServerForm(false, form.title, form.data!!.map {
            when (it) {
                is FormData.Article -> ServerFormData("text", it.article)
                is FormData.Image -> ServerFormData("image", it.image)
            }
        }, if (form.isWeb) form.link!! else form.capturedPackage)

        return networkApi.formUpdate(userId, formId, serverForm)
    }
}
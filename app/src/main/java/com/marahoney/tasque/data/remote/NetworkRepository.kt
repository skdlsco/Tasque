package com.marahoney.tasque.data.remote

import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.remote.response.EmptyResponse
import com.marahoney.tasque.data.remote.response.FormCreateResponse
import com.marahoney.tasque.data.remote.response.PostLoginResponse
import com.marahoney.tasque.data.remote.response.PostPictureResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface NetworkRepository {
    fun postPicture(picture: MultipartBody.Part): Single<PostPictureResponse>
    fun postLogin(imei: String): Single<PostLoginResponse>

    fun formCreate(userId: String, form: Form): Single<FormCreateResponse>
    fun formUpdate(userId: String, formId: String, form: Form): Single<EmptyResponse>
}
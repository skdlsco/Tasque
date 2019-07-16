package com.marahoney.tasque.data.remote

import com.marahoney.tasque.data.model.ServerForm
import com.marahoney.tasque.data.remote.response.EmptyResponse
import com.marahoney.tasque.data.remote.response.FormCreateResponse
import com.marahoney.tasque.data.remote.response.PostLoginResponse
import com.marahoney.tasque.data.remote.response.PostPictureResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface NetworkApi {

    @Multipart
    @POST("api/screenshot")
    fun postPicture(@Part picture: MultipartBody.Part): Single<PostPictureResponse>

    @POST("user/imei/{imei}")
    fun postLogin(@Path("imei") imei: String): Single<PostLoginResponse>

    @POST("form/{userId}")
    fun formCreate(@Path("userId") userId: String, @Body form: ServerForm): Single<FormCreateResponse>

    @PUT("form/{userId}/{formId}")
    fun formUpdate(@Path("userId") userId: String, @Path("formId") formId: String, @Body form: ServerForm): Single<EmptyResponse>

}

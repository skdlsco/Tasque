package com.marahoney.tasque.data.remote

import com.marahoney.tasque.data.remote.response.PostPictureResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NetworkApi {

    @Multipart
    @POST("api/screenshot")
    fun postPicture(@Part picture: MultipartBody.Part): Single<PostPictureResponse>
}

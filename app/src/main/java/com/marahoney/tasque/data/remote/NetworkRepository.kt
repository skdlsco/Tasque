package com.marahoney.tasque.data.remote

import com.marahoney.tasque.data.remote.response.PostPictureResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface NetworkRepository {
    fun postPicture(picture: MultipartBody.Part): Single<PostPictureResponse>
}
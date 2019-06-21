package com.marahoney.tasque.data.remote

import com.marahoney.tasque.data.remote.response.PostPictureResponse
import io.reactivex.Single
import okhttp3.MultipartBody

class NetworkRepositoryImpl(private val networkApi: NetworkApi) : NetworkRepository {

    override fun postPicture(picture: MultipartBody.Part): Single<PostPictureResponse> {
        return networkApi.postPicture(picture)
    }
}
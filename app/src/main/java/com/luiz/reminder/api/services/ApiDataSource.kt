package com.luiz.reminder.api.services

import com.luiz.reminder.api.constants.Constants
import com.luiz.reminder.api.responses.*
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiDataSource {

    @POST(Constants.LOGIN)
    fun login(@Body body: RequestBody): Single<LoginResponse>

    @POST(Constants.REGISTER)
    fun register(@Body body: RequestBody): Single<RegisterResponse>

    @POST(Constants.FORGOT_PASSWORD)
    fun forgotPassword(@Body body: RequestBody): Single<LoginResponse>

    @POST(Constants.RESET_PASSWORD)
    fun resetPassword(@Body body: RequestBody): Single<LoginResponse>

    @GET(Constants.USER + "/{id}")
    fun getUserInfo(@Path("id") id: String): Single<UserResponse>

    @PUT(Constants.USER + "/{id}")
    fun updateUser(@Body body: RequestBody, @Path("id") id: String): Single<UserResponse>

    @POST(Constants.NOTES)
    fun registerNote(@Body body: RequestBody): Single<NoteResponse>

    @GET(Constants.NOTES)
    fun getAllNotes(): Single<NotesResponse>

    @GET(Constants.NOTES + "/{id}")
    fun getNote(@Path("id") id: String): Single<NoteResponse>

    @POST(Constants.FCM_TOKEN)
    fun sendFCMToken(@Body body: RequestBody): Single<FCMResponse>
}
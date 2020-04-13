package com.luiz.reminder.api.services

import com.luiz.reminder.api.responses.*
import com.luiz.reminder.di.DaggerApiComponent
import io.reactivex.Single
import okhttp3.RequestBody
import javax.inject.Inject

class ApiRepository {
    @Inject
    lateinit var api: ApiDataSource

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun register(body: RequestBody): Single<RegisterResponse> {
        return api.register(body)
    }

    fun login(body: RequestBody): Single<LoginResponse> {
        return api.login(body)
    }

    fun resetPassword(body: RequestBody): Single<LoginResponse> {
        return api.resetPassword(body)
    }

    fun sendToken(body: RequestBody): Single<LoginResponse> {
        return api.forgotPassword(body)
    }

    fun getUserInfo(id: String): Single<UserResponse> {
        return api.getUserInfo(id)
    }

    fun updateUser(body: RequestBody, id: String): Single<UserResponse> {
        return api.updateUser(body, id)
    }

    fun registerNote(body: RequestBody): Single<NoteResponse> {
        return api.registerNote(body)
    }

    fun getAllNotes(): Single<NotesResponse> {
        return api.getAllNotes()
    }

    fun getNote(id: String): Single<NoteResponse> {
        return api.getNote(id)
    }

    fun sendFCMToken(body: RequestBody): Single<FCMResponse> {
        return api.sendFCMToken(body)
    }
}
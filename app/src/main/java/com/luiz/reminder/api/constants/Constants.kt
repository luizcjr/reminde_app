package com.luiz.reminder.api.constants

object Constants {
    const val CONTENT_TYPE = "Content-Type: application/json"
    const val BASE = "http://192.168.99.1:3000/"

    const val LOGIN = "auth/authenticate"
    const val REGISTER = "auth/register"
    const val FORGOT_PASSWORD = "auth/forgot_password"
    const val RESET_PASSWORD = "auth/reset_password"
    const val USER = "user"
    const val NOTES = "notes"
    const val FCM_TOKEN = "notes/notification"
}
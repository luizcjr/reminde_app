package com.luiz.reminder.api.responses

import com.luiz.reminder.api.models.User

class LoginResponse (
    var user: User,
    var token: String
)
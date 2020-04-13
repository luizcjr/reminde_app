package com.luiz.reminder.api.models

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("_id")
    var id: String,
    var name: String,
    var email: String
)
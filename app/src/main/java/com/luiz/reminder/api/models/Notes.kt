package com.luiz.reminder.api.models

import com.google.gson.annotations.SerializedName

class Notes(
    @SerializedName("_id")
    var id: String,
    var title: String,
    var description: String,
    var date: String,
    var is_notified: Boolean
)
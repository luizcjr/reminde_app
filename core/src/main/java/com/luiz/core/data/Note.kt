package com.luiz.core.data

import com.google.gson.annotations.SerializedName

class Note(
    @SerializedName("_id")
    var id: String,
    var title: String,
    var description: String,
    var descriptionShort: String,
    var author: String,
    var isOpen: Boolean
)
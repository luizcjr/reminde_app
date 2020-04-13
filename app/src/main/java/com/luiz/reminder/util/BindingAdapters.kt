package com.luiz.reminder.util

import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText

import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:textChangedListener")
fun textChangedListener(editText: AppCompatEditText, textWatcher: MaskWatcher?) {
    editText.addTextChangedListener(textWatcher)
}

fun Date.toSimpleString(): String {
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(this)
}

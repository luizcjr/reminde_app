package com.luiz.reminder.util

import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import java.util.ArrayList

object Mask {
    var maskDate = "maskDate"

    val FORMAT_DATE = "##/##/####"
    val FORMAT_HOUR = "##:##"
    val FORMAT_CPF = "###.###.###-##"
    val FORMAT_CEP = "#####-###"
    val FORMAT_PHONE = "(##) #####-####"
    val FORMAT_TEL = "(##) ####-####"
    val FORMAT_CNPJ = "##.###.###/####-##"
    val FORMAT_MONEY = "####.##"

    fun setMask(editText: EditText, maskType: String) {
        val mask: String
        val digits: String

        when (maskType) {
            "maskDate" -> {
                mask = Mask.maskDate().get(0)
                digits = Mask.maskDate().get(1)
            }

            else -> {
                mask = ""
                digits = ""
            }
        }

        if (mask.length == 0) {
            return
        }

        val affineFormats = ArrayList<String>()
        affineFormats.add(mask)

        editText.keyListener = DigitsKeyListener.getInstance(digits)

        val listener = MaskedTextChangedListener.installOn(
            editText,
            affineFormats[affineFormats.size - 1],
            affineFormats,
            AffinityCalculationStrategy.PREFIX,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedText: String
                ) {
                    logValueListener(maskFilled, extractedValue, formattedText)
                }
            }
        )
    }

    private fun logValueListener(
        maskFilled: Boolean,
        extractedValue: String,
        formattedText: String
    ) {
        val className = "_res"
        Log.d(className, extractedValue)
        Log.d(className, maskFilled.toString())
        Log.d(className, formattedText)
    }

    private fun maskDate(): ArrayList<String> {
        val mask = ArrayList<String>()
        //mask
        mask.add("[00]/[00]/[0000]")
        //allowed digits
        mask.add("0123456789/")

        return mask
    }

    fun mask(ediTxt: EditText, mask: String): TextWatcher {
        return object : TextWatcher {
            internal var isUpdating: Boolean = false
            internal var old = ""

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = Mask.unmask(s.toString())
                var mascara = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }

                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && str.length > old.length) {
                        mascara += m
                        continue
                    }
                    try {
                        mascara += str.get(i)
                    } catch (e: Exception) {
                        break
                    }

                    i++
                }
                isUpdating = true
                ediTxt.setText(mascara)
                ediTxt.setSelection(mascara.length)
            }
        }
    }

    fun unmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace(
                "[(]".toRegex(), ""
            ).replace("[ ]".toRegex(), "").replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
    }
}
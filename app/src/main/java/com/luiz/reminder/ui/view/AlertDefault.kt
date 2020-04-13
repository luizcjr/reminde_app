package com.luiz.reminder.ui.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatButton
import com.luiz.reminder.R
import kotlinx.android.synthetic.main.layout_dialog.*

class AlertDefault(
    context: Context,
    var title: String?,
    var content: String,
    var error: Boolean
) : Dialog(context) {

    private var buttonList: ArrayList<AppCompatButton>? = null

    init {
        buttonList = ArrayList()

        // disable cancel
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_dialog)

        if (error) setErrorAlert() else setSuccessAlert()

        // update content
        alertContent.text = content

        // check button
        if (buttonList!!.size == 0) {
            addButton(
                context.getString(R.string.alert_ok_action),
                View.OnClickListener { hide() })
        }

        if (buttonList!!.size > 0) {
            actionsContainer.weightSum = buttonList!!.size.toFloat()
            for (button in buttonList!!) {
                actionsContainer.addView(button)
            }
        }
    }

    private fun setErrorAlert() {
        alertTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0)
        if (title == null) alertTitle.text =
            context.getString(R.string.alert_error_title) else updateTitleLabel()
    }

    private fun setSuccessAlert() {
        alertTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_success, 0, 0, 0)
        if (title == null) alertTitle.text =
            context.getString(R.string.alert_success_title) else updateTitleLabel()
    }

    private fun updateTitleLabel() {
        alertTitle.text = title
    }

    fun addButton(
        title: String?,
        clickListener: View.OnClickListener?
    ) {
        addButton(title, 0, clickListener)
    }

    fun addButton(
        title: String?,
        styleId: Int,
        clickListener: View.OnClickListener?
    ) {
        var styleId = styleId
        if (styleId == 0) {
            styleId = R.style.ButtonAlert
        }
        val button = AppCompatButton(
            ContextThemeWrapper(context, styleId),
            null,
            0
        )
        button.text = title
        button.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        button.minWidth = 0
        if (clickListener != null) {
            button.setOnClickListener(clickListener)
        }
        buttonList!!.add(button)
    }
}
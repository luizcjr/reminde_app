package com.luiz.reminder.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Window
import android.widget.Toast
import androidx.annotation.AnimRes
import com.google.gson.Gson
import com.luiz.reminder.R
import com.luiz.reminder.ReminderApplication
import com.luiz.reminder.api.models.User
import com.luiz.reminder.ui.adapters.NoResultAdapter
import com.luiz.reminder.ui.view.AlertDefault
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utils {
    private var dialog: Dialog? = null

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    inline fun <reified T : Activity> Context.openActivity(
        options: Bundle? = null,
        finishWhenOpen: Boolean = false,
        @AnimRes enterAnim: Int = R.anim.activity_slide_pop_vertical_open_in,
        @AnimRes exitAnim: Int = R.anim.activity_slide_pop_vertical_open_out,
        noinline f: Intent.() -> Unit = {}
    ) {

        val intent = Intent(this, T::class.java)
        intent.f()
        startActivity(intent, options)
        if (finishWhenOpen) (this as Activity).finish()
        (this as Activity).overridePendingTransition(enterAnim, exitAnim)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun loadingDialog(ctx: Context): Dialog {
        val loading = Dialog(ctx)
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loading.setContentView(R.layout.dialog_loading)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull<Window>(loading.window)
                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        loading.setCanceledOnTouchOutside(false)
        loading.setCancelable(false)
        return loading
    }

    private fun getSessionPreferences(): SharedPreferences {
        val ctx = ReminderApplication.context
        return ctx!!.getSharedPreferences("SESSION_PREFERENCES", Context.MODE_PRIVATE)
    }

    fun getApiToken(): String? {
        return getSessionPreferences().getString("API_TOKEN", null)
    }

    fun setApiToken(token: String?) {
        val mPreferences = getSessionPreferences()
        val editor = mPreferences.edit()
        editor.putString("API_TOKEN", token)
        editor.apply()
    }

    fun setApiFCMToken(token: String?) {
        val mPreferences: SharedPreferences =
            getSessionPreferences()
        val editor = mPreferences.edit()
        editor.putString("API_FCM_TOKEN", token)
        editor.apply()
    }

    fun getApiFCMToken(): String? {
        return getSessionPreferences()
            .getString("API_FCM_TOKEN", null)
    }

    fun getLastUserSession(): User? {
        return Gson().fromJson<User?>(getPref("lastUser", null), User::class.java)
    }

    fun setLastUserSession(user: User?) {
        val userJSON = Gson().toJson(user, User::class.java)
        putPref("lastUser", userJSON)
    }

    fun setLastUserSession(json: String) {
        putPref("lastUser", json)
    }

    fun putPref(key: String, value: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(ReminderApplication.context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun containsPref(context: Context?, key: String?): Boolean? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.contains(key)
    }

    fun getPref(key: String, defValue: String?): String? {
        val preferences =
            PreferenceManager.getDefaultSharedPreferences(ReminderApplication.context)
        return preferences.getString(key, defValue)
    }

    fun removePref(key: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(ReminderApplication.context)
        val ed = prefs.edit()
        ed.remove(key)
        ed.apply()
    }

    fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun onStartLoading(context: Context) {
        dialog = loadingDialog(context)
        dialog!!.show()
    }

    fun onStopLoading() {
        if (dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    fun alertError(message: String, context: Context) {
        val alertDefault = AlertDefault(
            context,
            "Erro!",
            message,
            true
        )
        alertDefault.show()
    }

    fun getMessageErrorObject(e: Throwable): String {
        var loadError = String()

        when (e) {
            is HttpException -> {
                val responseBody: ResponseBody =
                    e.response().errorBody()!!
                try {
                    val jObject = JSONObject(responseBody.string())
                    loadError = jObject.getString("error")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            is SocketTimeoutException -> {
                loadError = e.message!!
            }
            is IOException -> {
                loadError = e.message!!
            }
            else -> {
                loadError = e.message!!
            }
        }

        return loadError
    }

    fun noResultAdapter(context: Context, message: String, image: Int): NoResultAdapter {
        return NoResultAdapter(
            context,
            message,
            R.color.colorGreyAdapter,
            image,
            0
        )
    }

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun convertDateFormat(
        date: String?,
        initDateFormat: String?,
        endDateFormat: String?
    ): String? {
        return try {
            val initDate = SimpleDateFormat(initDateFormat).parse(date)
            val formatter = SimpleDateFormat(endDateFormat)
            formatter.format(initDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            "Erro ao obter data"
        }
    }

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun convertDateBrl(
        date: String?
    ): String? {
        return try {
            val initDate = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date)
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
            formatter.format(initDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            "Erro ao obter data"
        }
    }
}
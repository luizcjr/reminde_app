package com.luiz.reminder.ui.activities.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.luiz.reminder.R
import com.luiz.reminder.ui.activities.login.LoginActivity
import com.luiz.reminder.ui.activities.login.LoginViewModel
import com.luiz.reminder.ui.activities.main.MainActivity
import com.luiz.reminder.util.Utils

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Contador para que a splash abra a activity de login
        val handler = Handler()
        handler.postDelayed({
            if (Utils.getApiToken() != null) {
                startActivity(MainActivity::class.java)
            } else {
                startActivity(LoginActivity::class.java)
            }
        }, 2500)
    }

    //Função para abrir a activity de login
    private fun startActivity(redirect: Class<*>) {
        startActivity(Intent(this@SplashScreenActivity, redirect))
        finish()
    }

    //Quando o botão de retornar físico do aparelho é apertado, nada acontece para não atrapalhar a transição de tela
    override fun onBackPressed() {

    }
}

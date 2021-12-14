package com.example.sportsballistics.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.utils.launchActivity

class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (!SharedPrefUtil.getInstance().isUserLoggedIn)
            {
                val intent = Intent(this, SelectionActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                launchActivity<DashboardActivity> {  }
                finish()
            }
        }, 2000)

    }
}
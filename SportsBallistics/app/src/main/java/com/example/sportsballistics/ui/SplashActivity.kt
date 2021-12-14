package com.example.sportsballistics.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.utils.launchActivityFinish

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (AppSystem.getInstance().getCurrentUser() != null) {
                launchActivityFinish<DashboardActivity> {
                    this.putExtra(
                        URLIdentifiers.USER_ROLE,
                        AppSystem.getInstance().getCurrentUser()!!.loggedIn?.roleId
                    )
                }
            } else {
                launchActivityFinish<SelectionActivity> { }
            }
        }, 2000)

    }
}
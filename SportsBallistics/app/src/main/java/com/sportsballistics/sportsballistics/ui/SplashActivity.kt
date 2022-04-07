package com.sportsballistics.sportsballistics.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.sportsballistics.sportsballistics.AppSystem
import com.sportsballistics.sportsballistics.R

import com.sportsballistics.sportsballistics.data.api.URLIdentifiers
import com.sportsballistics.sportsballistics.ui.dashboard.DashboardActivity
import com.sportsballistics.sportsballistics.utils.launchActivityFinish

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
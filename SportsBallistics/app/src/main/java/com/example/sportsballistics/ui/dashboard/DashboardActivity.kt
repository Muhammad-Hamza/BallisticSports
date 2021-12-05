package com.example.sportsballistics.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.databinding.ActivityDashboardBinding
import com.example.sportsballistics.ui.dashboard.athletes.AthletesFragment
import com.example.sportsballistics.ui.dashboard.clubs.ClubFragment
import com.example.sportsballistics.ui.dashboard.my_account.AccountFragment
import com.example.sportsballistics.ui.dashboard.trainer.TrainerFragment
import com.example.sportsballistics.ui.dashboard.users.UserFragment
import com.example.sportsballistics.utils.AppConstant

class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        setCurrentFragment(ClubFragment())


        if (AppSystem.getInstance().getCurrentUser() != null) {
            when (AppSystem.getInstance().getCurrentUser().loggedIn!!.roleId) {
                AppConstant.ROLE_SUPER_PORTAL -> {
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_admin_menu)
                }
                AppConstant.ROLE_ATHLETES_PORTAL -> {
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_athletes_menu)
                }
                AppConstant.ROLE_CLUB_PORTAL -> {
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_club_menu)
                }
                AppConstant.ROLE_TRAINER_PORTAL -> {
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_trainer_menu)
                }
            }
        }


        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            if (AppSystem.getInstance().getCurrentUser().loggedIn!!.roleId != null) {
                when (AppSystem.getInstance().getCurrentUser().loggedIn!!.roleId) {
                    AppConstant.ROLE_SUPER_PORTAL -> {
                        when (it.itemId) {//Working Fine
                            R.id.club -> setCurrentFragment(ClubFragment())
                            R.id.trainer -> setCurrentFragment(TrainerFragment())
                            R.id.athlete -> setCurrentFragment(AthletesFragment())
                            R.id.users -> setCurrentFragment(UserFragment())
                            R.id.account -> setCurrentFragment(AccountFragment())
                        }
                        true
                    }
                    AppConstant.ROLE_ATHLETES_PORTAL -> { //Working Fine.
                        when (it.itemId) {
                            R.id.club -> setCurrentFragment(ClubFragment())
                            R.id.trainer -> setCurrentFragment(TrainerFragment())
                            R.id.athlete -> setCurrentFragment(AthletesFragment())
                            R.id.users -> setCurrentFragment(UserFragment())
                            R.id.account -> setCurrentFragment(AccountFragment())
                        }
                        true
                    }
                    AppConstant.ROLE_CLUB_PORTAL -> {//Working Fine
                        when (it.itemId) {
                            R.id.club -> setCurrentFragment(ClubFragment())
                            R.id.trainer -> setCurrentFragment(TrainerFragment())
                            R.id.athlete -> setCurrentFragment(AthletesFragment())
                            R.id.users -> setCurrentFragment(UserFragment())
                            R.id.account -> setCurrentFragment(AccountFragment())
                        }
                        true
                    }
                    AppConstant.ROLE_TRAINER_PORTAL -> {
                        when (it.itemId) {
                            R.id.club -> setCurrentFragment(ClubFragment())
                            R.id.athlete -> setCurrentFragment(AthletesFragment())
                            R.id.account -> setCurrentFragment(AccountFragment())
                        }
                        true
                    }

                }
            }
            true

        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}
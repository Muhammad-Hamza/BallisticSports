package com.sportsballistics.sportsballistics.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.SharedPrefUtil
import com.sportsballistics.sportsballistics.databinding.ActivitySelectionBinding
import com.sportsballistics.sportsballistics.ui.login.LoginActivity
import com.sportsballistics.sportsballistics.utils.AppConstant
import com.sportsballistics.sportsballistics.utils.launchActivityFinish

class SelectionActivity : AppCompatActivity()
{
    lateinit var binding: ActivitySelectionBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_selection);
//        if (!SharedPrefUtil.getInstance().isUserLoggedIn)
//        {
            binding.imgSelectTodd.setOnClickListener {
                SharedPrefUtil.getInstance().saveSportsType(AppConstant.TODDLER)
                launchActivityFinish<LoginActivity> { }
            }
            binding.imgSelectBB.setOnClickListener {
                SharedPrefUtil.getInstance().saveSportsType(AppConstant.BASEBALL)
                launchActivityFinish<LoginActivity> { }
            }
            binding.imgSelectQB.setOnClickListener {
                SharedPrefUtil.getInstance().saveSportsType(AppConstant.QB)
                launchActivityFinish<LoginActivity> { }
            }
            binding.imgSelectVB.setOnClickListener {
                SharedPrefUtil.getInstance().saveSportsType(AppConstant.VOLLEYBALL)
                launchActivityFinish<LoginActivity> { }
            }
//        }
    }
}
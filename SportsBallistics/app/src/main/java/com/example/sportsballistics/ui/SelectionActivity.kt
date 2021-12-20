package com.example.sportsballistics.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.databinding.ActivitySelectionBinding
import com.example.sportsballistics.ui.login.LoginActivity
import com.example.sportsballistics.utils.AppConstant
import com.example.sportsballistics.utils.launchActivity
import com.example.sportsballistics.utils.launchActivityFinish

class SelectionActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_selection);
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
    }
}
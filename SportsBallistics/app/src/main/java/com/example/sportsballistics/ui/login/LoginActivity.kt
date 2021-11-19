package com.example.sportsballistics.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.login.UserResponse
import com.example.sportsballistics.databinding.LoginBinding
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.utils.launchActivity

class LoginActivity : AppCompatActivity()
{
    private lateinit var binding:LoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.login);

        initViewModel()

        binding.btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin(){
        if (viewModel.validate(binding.etEmail.text.toString(), binding.etPassword.text.toString(),binding)) {
            viewModel.login(
                applicationContext,
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                object : LoginViewModel.onSignInCompleteListener {
                    override fun onSignInComplete(userResponse: UserResponse) {
                        if (userResponse!= null) {
                                AppSystem.getInstance().saveVerificationStatus(true)
                                AppSystem.getInstance().saveUser(userResponse)
//                                AppSystem.getInstance().cityList.addAll(userResponse.data.cities!!)
//                                AppSystem.getInstance().bannerList.addAll(userResponse.data.banners!!)
//                                AppSystem.getInstance().saveUserId(userResponse.data.id!!)
//                                AppSystem.getInstance().saveApiKey(userResponse.data.apiKey!!)
//                                AppSystem.getInstance()
//                                    .saveIsRemember(binding.switchRemember.isChecked)
//                                AppSystem.getInstance()
//                                    .saveDeliveryTimes(userResponse.data.deliveryTimes)
//                                if (intent.hasExtra("isFromOrder")) {
//                                    finish()
//                                    return
//                                }
                            launchActivity<DashboardActivity> {
                                this.putExtra(URLIdentifiers.USER_ROLE, userResponse.loggedIn?.roleId)
                            }
                            finish()

                        } else {
                            Toast.makeText(
                                applicationContext!!,
                                userResponse.message,
                                Toast.LENGTH_SHORT
                                          ).show()
                        }
                    }
                })
        }

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener {
            override fun dismissDialog() {
                binding.pgBar.visibility = View.GONE
            }

            override fun addDialog() {
                binding.pgBar.visibility = View.VISIBLE
            }

            override fun addErrorDialog() {
            }

            override fun addErrorDialog(msg: String?) {
                if (msg != null) {
                }
            }
        })
    }

}
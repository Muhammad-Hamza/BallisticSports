package com.example.sportsballistics.ui.dashboard.my_account

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.AccountResponse
import com.example.sportsballistics.data.remote.login.UserResponse
import com.example.sportsballistics.databinding.FragmentAccountBinding
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.utils.AppConstant
import com.google.gson.Gson

class AccountFragment : Fragment() {
    private lateinit var viewModel: AccountViewModel
    lateinit var binding: FragmentAccountBinding
    lateinit var mActivity: DashboardActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as DashboardActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_account, container, false
        );
        initViewModel()
        binding.root.findViewById<AppCompatImageView>(R.id.backClubList).setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        loadAssets()
        setData()

        setListeners()
        return binding.root
    }

    private fun setListeners() {
//        binding.rlAboutSportsBallistics.setOnClickListener { getAccountData("about-us") }
//        binding.rlPP.setOnClickListener { getAccountData("privacy-policy") }
//        binding.rlTerms.setOnClickListener { getAccountData("terms-use") }
//        binding.rlContactSupport.setOnClickListener { getAccountData("contact-support") }
//        binding.rlFAQs.setOnClickListener { getAccountData("faqs ") }
//        binding.rlLogout.setOnClickListener {
//            AppSystem.getInstance().logoutUser()
//            mActivity.logoutFromUser()
//        binding.rlFAQs.setOnClickListener { getAccountData("faqs") }
//
//        binding.txtLogout.setOnClickListener{
//            SharedPrefUtil.getInstance().logout()
//            requireActivity().launchActivity<LoginActivity> {  }
//            requireActivity().finish()
//        }
//        binding.txtChangePassword.setOnClickListener {
//            Navigation.findNavController(binding.root)
//                .navigate(R.id.action_accountFragment_to_changePasswordFragment)
//        }

        binding.rlAboutSportsBallistics.setOnClickListener { getAccountData("about-us") }
        binding.rlPP.setOnClickListener { getAccountData("privacy-policy") }
        binding.rlTerms.setOnClickListener { getAccountData("terms-use") }
        binding.rlContactSupport.setOnClickListener { getAccountData("contact-support") }
        binding.rlFAQs.setOnClickListener { getAccountData("faqs") }

        binding.txtLogout.setOnClickListener {
            AppSystem.getInstance().logoutUser()
            mActivity.logoutFromUser()
        }
        binding.txtChangePassword.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }
    }

    fun setData() {
        val user = SharedPrefUtil.getInstance().user
        binding.txtNameValue.setText(user.loggedIn?.fullname)
        binding.txtEmailValue.setText(user.loggedIn?.email)
//        binding.txtNumberValue.setText(user.loggedIn?.)
        binding.txtRoleValue.setText(getRole(user))
    }

    fun getRole(user:UserResponse):String{
        when(user.loggedIn?.roleId){
            "1"->{
                return "Trainer"
            }
            "2"->{
                return "Athlete"
            }
            "3"->{
                return "Super Admin"
            }
            "4"->{
                return "Club Admin"
            }
            else->{
                return "Unknown"
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener {
            override fun dismissDialog() {
                binding.progressBar.visibility = View.GONE
            }

            override fun addDialog() {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun addErrorDialog() {
                binding.progressBar.visibility = View.GONE
            }

            override fun addErrorDialog(msg: String?) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(binding.root.context, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getAccountData(slug: String) {
        viewModel.getSettings(
            requireContext(),
            slug,
            object : AccountViewModel.ContentFetchListener {
                override fun onFetched(content: AccountResponse) {
                    Log.d("ACCOUNT", Gson().toJson(content))
                    binding.progressBar.visibility = View.GONE
                    if (content.data != null && content.data.size > 0) {
                        val bundle = Bundle()
                        bundle.putString(AppConstant.INTENT_EXTRA_1, content.data.get(0)!!.title)
                        bundle.putString(AppConstant.INTENT_EXTRA_2, content.data.get(0)!!.content)
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_accountFragment_to_accountDetailFragment, bundle)
                    }
                }
            })
    }

    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType
        AppConstant.changeColor(binding.txtTotalTrainersText)
        AppConstant.changeColor(binding.txtLogout)
        AppConstant.changeColor(binding.txtAcountDetail)
        AppConstant.changeColor(binding.txtSportsBallistics)
        AppConstant.changeColor(binding.txtChangePassword)
        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
            }
        }

    }

}
package com.example.sportsballistics.ui.dashboard.dashboard

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.service.ServiceResponseModel
import com.example.sportsballistics.data.remote.dashboard.DashboardResponse
import com.example.sportsballistics.data.remote.dashboard.LoggedIn
import com.example.sportsballistics.databinding.FragmentDashboardBinding
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.ui.dashboard.athletes.AthletesFragment
import com.example.sportsballistics.ui.login.LoginActivity
import com.example.sportsballistics.utils.*
import com.example.sportsballistics.utils.AppUtils.Companion.showToast
import com.google.gson.Gson


class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dashboard, container, false
        )
        loadAssets()
        hideAllViews()
        initViewModel()
        binding.flTrainer.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dashboardFragment_to_trainerFragment)
        }
        binding.rlClub.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dashboardFragment_to_clubFragment)
        }

        binding.rlTotalAthletes.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dashboardFragment_to_athletesFragment)
        }
        binding.flNewAtheles.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dashboardFragment_to_athletesFragment)
        }
//        binding.llAthleteView.setOnClickListener {
//            (activity as DashboardActivity).add(AthletesFragment(), R.id.rlParent)
//        }
        loadAssets()

        return binding.root
    }

    private fun hideAllViews() {
        binding.rlClub.visibility = View.GONE
        binding.flTrainer.visibility = View.GONE
        binding.flNewAtheles.visibility = View.GONE
        binding.flAtheles.visibility = View.GONE
        binding.llProfile.visibility = View.GONE
    }

    private fun initViews(loggedIn: LoggedIn?, athleteDataModel: ServiceResponseModel?) {
        if (AppSystem.getInstance().getCurrentUser() != null && AppSystem.getInstance()
                .getCurrentUser()!!.loggedIn != null
        ) {
            when (AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId) {
                AppConstant.ROLE_TRAINER_PORTAL -> {
                    if (loggedIn != null) {
                        binding.txtLogin.setText(R.string.txt_welcome_trainer_admin)
                        binding.txtSADashboard.setText(R.string.txt_welcome_dashboard_trainer_admin)
                        binding.rlClub.visibility = View.GONE
                        binding.flTrainer.visibility = View.GONE
                        //TODO: Getting GOne for some reason like we are not getting data from backed: NEED DISCUSSION
                        binding.flNewAtheles.visibility = View.GONE

                        binding.flAtheles.visibility = View.VISIBLE
                        binding.llProfile.visibility = View.GONE
                        if (loggedIn!!.athleteCount != null)
                            binding.txtTotalAthletes.setText(loggedIn.athleteCount.toString())
                        else
                            binding.txtTotalAthletes.setText("0")
                    }
                }
                AppConstant.ROLE_CLUB_PORTAL -> {
                    if (loggedIn != null) {
                        binding.txtLogin.setText(R.string.txt_welcome_club_admin)
                        binding.txtSADashboard.setText(R.string.txt_welcome_dashboard_club_admin)
                        binding.rlClub.visibility = View.GONE
                        binding.flNewAtheles.visibility = View.GONE
                        binding.flTrainer.visibility = View.VISIBLE
                        binding.flAtheles.visibility = View.VISIBLE
                        binding.llProfile.visibility = View.GONE
                        if (loggedIn!!.athleteCount != null) {
                            binding.txtTotalAthletes.setText(loggedIn.athleteCount.toString())
                        } else {
                            binding.txtTotalAthletes.setText("0")
                        }
                        if (loggedIn!!.trainerCount != null) {
                            binding.txtTotalTrainers.setText(loggedIn.trainerCount.toString())
                        } else {
                            binding.txtTotalTrainers.setText("0")
                        }
                    }
                }
                AppConstant.ROLE_ATHLETES_PORTAL -> {
                    if (athleteDataModel != null) {
                        binding.txtLogin.setText(R.string.txt_welcome_athletes_admin)
                        binding.txtSADashboard.setText(
                            getString(R.string.txt_welcome_dashboard_athletes_admin).replace(
                                "{name}", athleteDataModel.data!!.athleticName?.fullname!!
                            )
                        )
                        binding.rlClub.visibility = View.GONE
                        binding.flTrainer.visibility = View.GONE
                        binding.flAtheles.visibility = View.GONE
                        binding.flNewAtheles.visibility = View.GONE
                        binding.llProfile.visibility = View.VISIBLE
                        binding.tvName.setText(
                            athleteDataModel.data!!.athleticName?.fullname
                        )
                        if (athleteDataModel.data != null && !TextUtils.isEmpty(athleteDataModel.data.clubname)) {
                            binding.tvClub.setText(
                                AppFunctions.getSpannableText(
                                    getString(R.string.txt_athletes_club_name),
                                    "{{clubName}}",
                                    "${athleteDataModel.data.clubname}"
                                )
                            )
                            binding.tvClub.visibility = View.VISIBLE
                        } else {
                            binding.tvClub.visibility = View.GONE
                        }
//                        if (athleteDataModel.data != null && !TextUtils.isEmpty(athleteDataModel.data.)) {
//                            binding.tvTrainer.setText(
//                                AppFunctions.getSpannableText(
//                                    getString(R.string.txt_athletes_trainer),
//                                    "{{trainer}}",
//                                    ""
//                                )
//                            )
//                            binding.tvTrainer.visibility = View.VISIBLE
//                        } else {
                        binding.tvTrainer.visibility = View.GONE
//                        }
                        if (athleteDataModel.data != null && athleteDataModel.data.athleticName != null && !TextUtils.isEmpty(
                                athleteDataModel.data.athleticName.age
                            )
                        ) {
                            binding.tvAge.setText(
                                AppFunctions.getSpannableText(
                                    getString(R.string.txt_athletes_age),
                                    "{{age}}",
                                    "${athleteDataModel.data.athleticName?.age}"
                                )
                            )
                            binding.tvAge.visibility = View.VISIBLE
                        } else {
                            binding.tvAge.visibility = View.GONE
                        }
                        if (athleteDataModel.data != null && athleteDataModel.data.athleticName != null && !TextUtils.isEmpty(
                                athleteDataModel.data.athleticName.grade
                            )
                        ) {
                            binding.tvGrade.setText(
                                AppFunctions.getSpannableText(
                                    getString(R.string.txt_athletes_grade),
                                    "{{grade}}",
                                    "${athleteDataModel.data.athleticName?.grade}"
                                )
                            )
                            binding.tvGrade.visibility = View.VISIBLE
                        } else {
                            binding.tvGrade.visibility = View.GONE
                        }
                        if (AppSystem.getInstance()
                                .getCurrentUser()!!.loggedIn!!.profileImage != null
                        ) {
//                            loadImage(
//                                AppSystem.getInstance()
//                                    .getCurrentUser()!!.loggedIn!!.profileImage!!,
//                                binding.ivUserImage
//                            )
                            Glide.with(this).load(
                                AppSystem.getInstance()
                                    .getCurrentUser()!!.loggedIn!!.profileImage!!
                            ).placeholder(R.mipmap.ic_temp_avatar).into(binding.ivUserImage)
                        } else {
                            Glide.with(this).load(R.mipmap.ic_temp_avatar).into(binding.ivUserImage)
                        }
                    }
                }
                AppConstant.ROLE_SUPER_PORTAL -> {
                    if (loggedIn != null) {
                        binding.txtLogin.setText(R.string.txt_welcome_super_admin)
                        binding.txtSADashboard.setText(R.string.txt_welcome_dashboard_super_admin)
                        binding.rlClub.visibility = View.VISIBLE
                        binding.flTrainer.visibility = View.VISIBLE
                        binding.flAtheles.visibility = View.VISIBLE
                        binding.flNewAtheles.visibility = View.GONE
                        binding.llProfile.visibility = View.GONE

                        if (loggedIn!!.caCount != null) {
                            binding.txtTotalClubs.setText(loggedIn.caCount.toString())
                        } else {
                            binding.txtTotalClubs.setText("0")
                        }
                        if (loggedIn!!.athleteCount != null) {
                            binding.txtTotalAthletes.setText(loggedIn.athleteCount.toString())
                        } else {
                            binding.txtTotalAthletes.setText("0")
                        }
                        if (loggedIn!!.trainerCount != null) {
                            binding.txtTotalTrainers.setText(loggedIn.trainerCount.toString())
                        } else {
                            binding.txtTotalTrainers.setText("0")
                        }
                    }
                }
                else -> {
                    showToast(R.string.txt_invalid_login_content)
                }
            }
        } else {
            if (context != null)
                showToast(R.string.txt_user_not_found_content)
            requireActivity().launchActivityFinish<LoginActivity> {
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener {
            override fun dismissDialog() {
                binding.progressBar.visibility = View.GONE
                binding.llDashboard.visibility = View.VISIBLE
            }

            override fun addDialog() {
                binding.progressBar.visibility = View.VISIBLE
                binding.llDashboard.visibility = View.GONE
            }

            override fun addErrorDialog() {
                binding.progressBar.visibility = View.GONE
                binding.llDashboard.visibility = View.VISIBLE
            }

            override fun addErrorDialog(msg: String?) {
                binding.progressBar.visibility = View.GONE
                binding.llDashboard.visibility = View.VISIBLE
                showToast(msg!!)
            }
        })

        if (AppSystem.getInstance()
                .getCurrentUser()!!.loggedIn!!.roleId.equals(AppConstant.ROLE_ATHLETES_PORTAL)
        ) {
            viewModel.getAthleteInfo(
                binding.root.context,
                object : DashboardViewModel.AthleteContentFetchListener {
                    override fun onFetched(anyObject: ServiceResponseModel) {
                        if (anyObject is ServiceResponseModel) {
                            initViews(null, anyObject)
                        }
                        binding.progressBar.visibility = View.GONE
                        binding.llDashboard.visibility = View.VISIBLE
                    }

                }, AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.id!!
            )
        } else {
            viewModel.getDashboard(
                requireContext(),
                object : DashboardViewModel.DashboardFetchListener {
                    override fun onFetched(content: DashboardResponse) {
                        Log.d(DashboardFragment::javaClass.name, Gson().toJson(content))
                        //TODO Asher bind this data to UI
                        if (content != null && content.loggedIn != null) {
                            initViews(content.loggedIn, null)
                            binding.progressBar.visibility = View.GONE
                            binding.llDashboard.visibility = View.VISIBLE
                        } else {
                            showToast(R.string.txt_user_not_found_normal_content)
                            Navigation.findNavController(binding.root).navigateUp()
                        }
                    }
                })
        }
    }

    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType

//        AppConstant.changeColor(binding.txtTotalClubs)
        AppConstant.changeColor(binding.txtTotalClubsText)
//        AppConstant.changeColor(binding.txtTotalTrainers)
        AppConstant.changeColor(binding.txtTotalTrainersText)
//        AppConstant.changeColor(binding.txtTotalAthletes)
        AppConstant.changeColor(binding.txtTotalAthletesText)
//        AppConstant.changeColor(binding.txtNewTotalAthletes)
        AppConstant.changeColor(binding.txtNewTotalAthletesText)
        AppConstant.changeColor(binding.tvName)
        AppConstant.changeColor(binding.tvClub)
        AppConstant.changeColor(binding.tvTrainer)
        AppConstant.changeColor(binding.tvAge)
        AppConstant.changeColor(binding.tvGrade)
        AppConstant.changeColor(binding.txtLogin)
        AppConstant.changeColor(binding.txtSADashboard)


        //        AppConstant.changeColor(binding.imgTotalClubs)
//        AppConstant.changeColor(binding.imgTotalTrainers)
//        AppConstant.changeColor(binding.imgTotalAthletes)
//        AppConstant.changeColor(binding.imgNewTotalAthletes)
        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo).into(binding.imgLogo)
                binding.llProfileLayout.setBackgroundResource(R.drawable.ic_bb_dash_profile)

                Glide.with(binding.root).load(R.drawable.bb_club_selected)
                    .into(binding.imgTotalClubs)
                Glide.with(binding.root).load(R.drawable.bb_trainers_selected)
                    .into(binding.imgTotalTrainers)
                Glide.with(binding.root).load(R.drawable.bb_athlete_selected)
                    .into(binding.imgTotalAthletes)
                Glide.with(binding.root).load(R.drawable.bb_athlete_selected)
                    .into(binding.imgNewTotalAthletes)
            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo).into(binding.imgLogo)
                binding.llProfileLayout.setBackgroundResource(R.drawable.ic_vb_dash_profile)

                Glide.with(binding.root).load(R.drawable.vb_clubs_selected)
                    .into(binding.imgTotalClubs)
                Glide.with(binding.root).load(R.drawable.vb_trainer_selected)
                    .into(binding.imgTotalTrainers)
                Glide.with(binding.root).load(R.drawable.vb_athlete_selected)
                    .into(binding.imgTotalAthletes)
                Glide.with(binding.root).load(R.drawable.vb_athlete_selected)
                    .into(binding.imgNewTotalAthletes)
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
                binding.llProfileLayout.setBackgroundResource(R.drawable.ic_dash_profile)

                Glide.with(binding.root).load(R.drawable.ic_dashboard_super_admin_total_clubs)
                    .into(binding.imgTotalClubs)
                Glide.with(binding.root).load(R.drawable.ic_total_trainers)
                    .into(binding.imgTotalTrainers)
                Glide.with(binding.root)
                    .load(R.drawable.ic_nav_dashboard_super_admin_atheles_selected)
                    .into(binding.imgTotalAthletes)
                Glide.with(binding.root)
                    .load(R.drawable.ic_nav_dashboard_super_admin_atheles_selected)
                    .into(binding.imgNewTotalAthletes)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo).into(binding.imgLogo)
                binding.llProfileLayout.setBackgroundResource(R.drawable.ic_qb_dash_profile)

                Glide.with(binding.root).load(R.drawable.qb_club_selected)
                    .into(binding.imgTotalClubs)
                Glide.with(binding.root).load(R.drawable.qb_trainer_selected)
                    .into(binding.imgTotalTrainers)
                Glide.with(binding.root).load(R.drawable.qb_athlete_selected)
                    .into(binding.imgTotalAthletes)
                Glide.with(binding.root).load(R.drawable.qb_athlete_selected)
                    .into(binding.imgNewTotalAthletes)
            }
        }
    }

}
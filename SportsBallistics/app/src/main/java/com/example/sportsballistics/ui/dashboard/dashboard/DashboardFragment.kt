package com.example.sportsballistics.ui.dashboard.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.dashboard.DashboardResponse
import com.example.sportsballistics.data.remote.dashboard.LoggedIn
import com.example.sportsballistics.data.remote.service.ServiceResponseModel
import com.example.sportsballistics.databinding.FragmentDashboardBinding
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.ui.dashboard.athletes.AthletesFragment
import com.example.sportsballistics.ui.login.LoginActivity
import com.example.sportsballistics.utils.*
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
        );
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
        binding.llAthleteView.setOnClickListener {
            (activity as DashboardActivity).add(AthletesFragment(), R.id.rlParent)
        }

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
                                "{name}",
                                athleteDataModel.data!!.athletic_name.fullname
                            )
                        )
                        binding.rlClub.visibility = View.GONE
                        binding.flTrainer.visibility = View.GONE
                        binding.flAtheles.visibility = View.GONE
                        binding.flNewAtheles.visibility = View.GONE
                        binding.llProfile.visibility = View.VISIBLE
                        binding.tvName.setText(
                            athleteDataModel.data!!.athletic_name.fullname
                        )
                        binding.tvClub.setText(
                            AppFunctions.getSpannableText(
                                getString(R.string.txt_athletes_club_name),
                                "{{clubName}}",
                                "${athleteDataModel.data.clubname}"
                            )
                        )
                        binding.tvTrainer.setText(
                            AppFunctions.getSpannableText(
                                getString(R.string.txt_athletes_trainer),
                                "{{trainer}}",
                                "Mike Thomas"
                            )
                        )
                        binding.tvAge.setText(
                            AppFunctions.getSpannableText(
                                getString(R.string.txt_athletes_age),
                                "{{age}}",
                                "${athleteDataModel.data.athletic_name.age}"
                            )
                        )
                        binding.tvGrade.setText(
                            AppFunctions.getSpannableText(
                                getString(R.string.txt_athletes_grade),
                                "{{grade}}",
                                "${athleteDataModel.data.athletic_name.grade}"
                            )
                        )
                        if (AppSystem.getInstance()
                                .getCurrentUser()!!.loggedIn!!.profileImage != null
                        ) {
                            loadImage(
                                AppSystem.getInstance()
                                    .getCurrentUser()!!.loggedIn!!.profileImage!!,
                                binding.ivUserImage
                            )
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
                    Toast.makeText(
                        requireContext(),
                        "Invalid Login\n please login with your credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().launchActivityFinish<LoginActivity> {
                    }

                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "User not found\n please login with your credentials",
                Toast.LENGTH_SHORT
            ).show()
            requireActivity().launchActivityFinish<LoginActivity> {
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
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
                        if (content != null && content.loggedIn != null)
                            initViews(content.loggedIn, null)
                        else {
                            Toast.makeText(
                                binding.root.context,
                                "User not found",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            Navigation.findNavController(binding.root).navigateUp()
                        }
                    }
                })
        }
    }

    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType
        AppConstant.changeColor(binding.imgTotalClubs)
        AppConstant.changeColor(binding.imgTotalTrainers)
        AppConstant.changeColor(binding.imgTotalAthletes)
        AppConstant.changeColor(binding.imgNewTotalAthletes)
        AppConstant.changeColor(binding.txtTotalClubs)
        AppConstant.changeColor(binding.txtTotalClubsText)
        AppConstant.changeColor(binding.txtTotalTrainers)
        AppConstant.changeColor(binding.txtTotalTrainersText)
        AppConstant.changeColor(binding.txtTotalAthletes)
        AppConstant.changeColor(binding.txtTotalAthletesText)
        AppConstant.changeColor(binding.txtNewTotalAthletes)
        AppConstant.changeColor(binding.txtNewTotalAthletesText)
        AppConstant.changeColor(binding.tvName)
        AppConstant.changeColor(binding.tvClub)
        AppConstant.changeColor(binding.tvTrainer)
        AppConstant.changeColor(binding.tvAge)
        AppConstant.changeColor(binding.tvGrade)
        AppConstant.changeColor(binding.txtLogin,requireContext())
        AppConstant.changeColor(binding.txtSADashboard,requireContext())
        AppSystem.getInstance().setStatusColor(requireActivity())
        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo).into(binding.imgLogo)
                binding.llProfileLayout.setBackgroundResource(R.drawable.ic_bb_dash_profile)
            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo).into(binding.imgLogo)
                binding.llProfileLayout.setBackgroundResource(R.drawable.ic_vb_dash_profile)
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
                binding.llProfileLayout.setBackgroundResource(R.drawable.ic_dash_profile)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo).into(binding.imgLogo)
                binding.llProfileLayout.setBackgroundResource(R.drawable.ic_qb_dash_profile)
            }
        }
    }

}
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
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.dashboard.DashboardResponse
import com.example.sportsballistics.data.remote.dashboard.LoggedIn
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
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dashboard, container, false
        );
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

    private fun hideAllViews(){
        binding.rlClub.visibility = View.GONE
        binding.flTrainer.visibility = View.GONE
        binding.flNewAtheles.visibility = View.GONE
        binding.flAtheles.visibility = View.GONE
        binding.llProfile.visibility = View.GONE
    }
    private fun initViews(loggedIn: LoggedIn?) {
        if (AppSystem.getInstance().getCurrentUser() != null && AppSystem.getInstance()
                .getCurrentUser().loggedIn != null
        ) {
            when (AppSystem.getInstance().getCurrentUser().loggedIn!!.roleId) {
                AppConstant.ROLE_TRAINER_PORTAL -> {
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
                AppConstant.ROLE_CLUB_PORTAL -> {
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
                AppConstant.ROLE_ATHLETES_PORTAL -> {
                    binding.txtLogin.setText(R.string.txt_welcome_athletes_admin)
                    binding.txtSADashboard.setText(
                        getString(R.string.txt_welcome_dashboard_athletes_admin).replace(
                            "{name}",
                            AppSystem.getInstance().getCurrentUser().loggedIn!!.fullname!!
                        )
                    )
                    binding.rlClub.visibility = View.GONE
                    binding.flTrainer.visibility = View.GONE
                    binding.flAtheles.visibility = View.GONE
                    binding.flNewAtheles.visibility = View.GONE
                    binding.llProfile.visibility = View.VISIBLE
                    binding.tvName.setText(
                        AppSystem.getInstance().getCurrentUser().loggedIn!!.fullname!!
                    )
                    binding.tvClub.setText(
                        AppFunctions.getSpannableText(
                            getString(R.string.txt_athletes_club_name),
                            "{{clubName}}",
                            "Club Name 1"
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
                            AppSystem.getInstance().getCurrentUser().loggedIn!!.age!!
                        )
                    )
                    binding.tvGrade.setText(
                        AppFunctions.getSpannableText(
                            getString(R.string.txt_athletes_grade),
                            "{{grade}}",
                            AppSystem.getInstance().getCurrentUser().loggedIn!!.grade!!
                        )
                    )
                    if (AppSystem.getInstance().getCurrentUser().loggedIn!!.profileImage != null) {
                        loadImage(
                            AppSystem.getInstance().getCurrentUser().loggedIn!!.profileImage!!,
                            binding.ivUserImage
                        )
                    }
                }
                AppConstant.ROLE_SUPER_PORTAL -> {
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
            }

            override fun addDialog() {
            }

            override fun addErrorDialog() {
            }

            override fun addErrorDialog(msg: String?) {
            }
        })

        viewModel.getDashboard(
            requireContext(),
            object : DashboardViewModel.DashboardFetchListener {
                override fun onFetched(content: DashboardResponse) {
                    Log.d(DashboardFragment::javaClass.name, Gson().toJson(content))
                    //TODO Asher bind this data to UI
                    if (content != null && content.loggedIn != null)
                        initViews(content.loggedIn)
                    else {
                        Toast.makeText(binding.root.context, "User not found", Toast.LENGTH_SHORT)
                            .show()
                        Navigation.findNavController(binding.root).navigateUp()
                    }
                }
            })
    }
}
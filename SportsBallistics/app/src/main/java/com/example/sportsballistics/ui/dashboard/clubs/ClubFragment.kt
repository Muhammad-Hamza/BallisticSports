package com.example.sportsballistics.ui.dashboard.clubs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.FragmentClubBinding
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.ui.login.LoginActivity
import com.example.sportsballistics.utils.*

class ClubFragment : Fragment() {
    lateinit var binding: FragmentClubBinding
    private lateinit var viewModel: ClubListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_club, container, false
        );
        initViewModel()
        initViews()
        binding.rlClub.setOnClickListener {
            binding.clubListLayout.clubListLayoutParent.visibility = View.VISIBLE
            binding.llDashboard.visibility = View.GONE
        }
        binding.clubListLayout.backClubList.setOnClickListener {
            binding.clubListLayout.clubListLayoutParent.visibility = View.GONE
            binding.llDashboard.visibility = View.VISIBLE
        }
        return binding.root
    }

    private fun initViews() {
        if (AppSystem.getInstance().getCurrentUser() != null) {
            when (AppSystem.getInstance().getCurrentUser().loggedIn!!.roleId) {
                AppConstant.ROLE_TRAINER_PORTAL -> {
                    binding.txtLogin.setText(R.string.txt_welcome_trainer_admin)
                    binding.txtSADashboard.setText(R.string.txt_welcome_dashboard_trainer_admin)
                    binding.rlClub.visibility = View.GONE
                    binding.flTrainer.visibility = View.GONE
                    binding.flNewAtheles.visibility = View.VISIBLE
                    binding.flAtheles.visibility = View.VISIBLE
                    binding.llProfile.visibility = View.GONE
                }
                AppConstant.ROLE_CLUB_PORTAL -> {
                    binding.txtLogin.setText(R.string.txt_welcome_club_admin)
                    binding.txtSADashboard.setText(R.string.txt_welcome_dashboard_club_admin)
                    binding.rlClub.visibility = View.GONE
                    binding.flNewAtheles.visibility = View.GONE
                    binding.flTrainer.visibility = View.VISIBLE
                    binding.flAtheles.visibility = View.VISIBLE
                    binding.llProfile.visibility = View.GONE
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


    private fun initRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context)
        val mAdapter = ClubListAdapter(context, null, object : ClubListAdapter.OnItemClickListener {
            override fun onEditClick(adapterType: Int, user: UsersItem) {
            }

            override fun onViewClick(adapterType: Int, user: UsersItem) {
                (activity as DashboardActivity).add(CreateClubFragment(), R.id.flFragment)
            }

            override fun onDeleteClick(adapterType: Int, user: UsersItem) {
            }
        })
        binding.clubListLayout.recyclerView.layoutManager = mLayoutManager
        binding.clubListLayout.recyclerView.adapter = mAdapter
    }

    fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ClubListViewModel::class.java)
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

        viewModel.getContent(
            requireContext(),
            URLIdentifiers.CLUB_CONTENT,
            "",
            object : ClubListViewModel.ContentFetchListener {
                override fun onFetched(content: ClubResponse) {
//                initRecyclerView(content.content?.users as MutableList<UsersItem>)
                    initRecyclerView()
                }
            })
    }
}
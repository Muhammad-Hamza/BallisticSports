package com.example.sportsballistics.ui.dashboard.trainer

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.FragmentTrainerBinding
import com.example.sportsballistics.ui.dashboard.athletes.AthletesViewModel
import com.example.sportsballistics.ui.dashboard.dashboard.DashboardViewModel
import com.example.sportsballistics.utils.AppConstant

class TrainerFragment : Fragment() {
    lateinit var binding: FragmentTrainerBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_trainer, container, false
        );
        initViewModel()

        binding.clubListHeader.txtClubName.text = "Trainer Name"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAssets()
        when (AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId) {
            AppConstant.ROLE_CLUB_PORTAL -> {
                binding.llAddAthlete.visibility = View.VISIBLE
                binding.llAddTrainer.visibility = View.VISIBLE
            }
            AppConstant.ROLE_SUPER_PORTAL -> {
                binding.llAddAthlete.visibility = View.GONE
                binding.llAddTrainer.visibility = View.GONE
            }
            AppConstant.ROLE_ATHLETES_PORTAL -> {
                binding.llAddAthlete.visibility = View.GONE
                binding.llAddTrainer.visibility = View.GONE
            }
            AppConstant.ROLE_TRAINER_PORTAL -> {
                binding.llAddAthlete.visibility = View.VISIBLE
                binding.llAddTrainer.visibility = View.GONE
            }
            else -> {
                binding.llAddAthlete.visibility = View.GONE
                binding.llAddTrainer.visibility = View.GONE
            }
        }
        binding.llAddTrainer.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(
                AppConstant.INTENT_SCREEN_TYPE,
                AppConstant.INTENT_SCREEN_TYPE_ADD
            )
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_trainerFragment_to_createTrainerFragment, bundle)
        }

        binding.llAddAthlete.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(
                AppConstant.INTENT_SCREEN_TYPE,
                AppConstant.INTENT_SCREEN_TYPE_ADD
            )
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_trainerFragment_to_createAthleteFragment, bundle)
        }

        binding.etReason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s) && s!!.length >= 3) {
                    getTrainerFromServer(s.toString())
                } else {
                    if (TextUtils.isEmpty(s))
                        getTrainerFromServer("")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
    }


    private fun initRecyclerView(users: List<UsersItem?>?) {
        val mLayoutManager = LinearLayoutManager(context)
        var mAdapter =
            TrainerAdapter(
                context,
                users,
                object : TrainerAdapter.OnItemClickListener {
                    override fun onEditClick(adapterType: Int, user: UsersItem) {
                        val bundle = Bundle()
                        bundle.putInt(
                            AppConstant.INTENT_SCREEN_TYPE,
                            AppConstant.INTENT_SCREEN_TYPE_EDIT
                        )
                        bundle.putString(AppConstant.INTENT_EXTRA_1, user.id)
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_trainerFragment_to_createTrainerFragment, bundle)
                    }

                    override fun onViewClick(adapterType: Int, user: UsersItem) {
                        val bundle = Bundle()
                        bundle.putInt(
                            AppConstant.INTENT_SCREEN_TYPE,
                            AppConstant.INTENT_SCREEN_TYPE_VIEW
                        )
                        bundle.putString(AppConstant.INTENT_EXTRA_1, user.id)
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_trainerFragment_to_createTrainerFragment, bundle)
                    }

                    override fun onDeleteClick(adapterType: Int, user: UsersItem) {
                        MaterialDialog(binding.root.context)
                            .title(null, "Want to delete!")
                            .message(null, "Do you want to delete this Trainer?")
                            .positiveButton(null, "YES") {
                                viewModel.deleteTrainer(requireContext(), user.id!!, object :
                                    AthletesViewModel.ContentFetchListener {
                                    override fun onFetched(anyObject: Any) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Athlete Deleted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        getTrainerFromServer("")
                                    }
                                })
                            }.negativeButton(null, "NO") {

                            }.show()
                    }
                })
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.adapter = mAdapter
    }

    fun initViewModel() {
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
            }
        })
        getTrainerFromServer("")
    }

    private fun getTrainerFromServer(searchContent: String) {
        viewModel.getContent(
            requireContext(),
            URLIdentifiers.TRAINER_CONTENT,
            searchContent,
            object : DashboardViewModel.ContentFetchListener {
                override fun onFetched(content: ClubResponse) {
                    if (content != null && content.content != null && content.content.users != null && content.content.users.size > 0)
                        initRecyclerView(content.content.users)
                    else
                        initRecyclerView(ArrayList<UsersItem>())
                }
            })
    }


    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType

        AppConstant.changeColor(binding.txtTotalTrainersText)
        AppConstant.changeColor(binding.clubListHeader.txtSerialNo)
        AppConstant.changeColor(binding.clubListHeader.txtClubName)
        AppConstant.changeColor(binding.clubListHeader.txtAction)
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
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo)
                    .into(binding.imgLogo)
            }
        }
    }
}
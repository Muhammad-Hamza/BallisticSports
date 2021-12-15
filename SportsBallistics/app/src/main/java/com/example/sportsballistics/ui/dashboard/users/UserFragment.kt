package com.example.sportsballistics.ui.dashboard.users

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.FragmentUserBinding
import com.example.sportsballistics.ui.dashboard.dashboard.ClubListAdapter
import com.example.sportsballistics.ui.dashboard.dashboard.DashboardViewModel
import com.example.sportsballistics.utils.AppConstant
import com.google.gson.Gson

class UserFragment : Fragment() {
    lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        initViewModel()
        binding.clubListHeader.txtClubName.text = "User Name"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.etReason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s) && s!!.length >= 3) {
                    getContent(s.toString())
                } else {
                    if (TextUtils.isEmpty(s))
                        getContent("")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.llAddTrainer.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(
                AppConstant.INTENT_SCREEN_TYPE,
                AppConstant.INTENT_SCREEN_TYPE_ADD
            )
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userFragment_to_createTrainerFragment, bundle)
        }

        binding.llAddAthlete.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(
                AppConstant.INTENT_SCREEN_TYPE,
                AppConstant.INTENT_SCREEN_TYPE_ADD
            )
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userFragment_to_createAthleteFragment, bundle)
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
            }
        })

        getContent("")
    }

    private fun getContent(searchKey: String) {
        viewModel.getContent(requireContext(), URLIdentifiers.USER_CONTENT, searchKey, object :
            DashboardViewModel.ContentFetchListener {
            override fun onFetched(content: ClubResponse) {
                if (content != null && content.content != null && content.content.users != null && content.content.users.size > 0) {
                    initRecyclerView(content.content?.users as MutableList<UsersItem>)
                } else {
                    initRecyclerView(ArrayList<UsersItem>())
                }
            }
        })
    }

    private fun initRecyclerView(list: MutableList<UsersItem>) {
        val mLayoutManager = LinearLayoutManager(context)
        val mAdapter = UserAdapter(context, list, object :
            UserAdapter.OnItemClickListener {
            override fun onEditClick(adapterType: Int, user: UsersItem) {
                if (user != null && user.role_name != null) {
                    val args = Bundle()
                    args.putString(AppConstant.INTENT_EXTRA_1, user.id)
                    args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_EDIT)
                    when (user.role_name) {
                        "Athlete" -> {
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_userFragment_to_createAthleteFragment, args)
                        }
                        "Trainer" -> {
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_userFragment_to_createTrainerFragment, args)
                        }
                    }
                }
            }

            override fun onViewClick(adapterType: Int, user: UsersItem) {
                if (user != null && user.role_name != null) {
                    val args = Bundle()
                    args.putString(AppConstant.INTENT_EXTRA_1, user.id)
                    args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_VIEW)
                    when (user.role_name) {
                        "Athlete" -> {
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_userFragment_to_createAthleteFragment, args)
                        }
                        "Trainer" -> {
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_userFragment_to_createTrainerFragment, args)
                        }
                    }
                }
            }

            override fun onDeleteClick(adapterType: Int, user: UsersItem) {
                MaterialDialog(binding.root.context).title(null, "Want to delete!")
                    .message(null, "Do you want to delete this User?").positiveButton(null, "YES") {

                    }.negativeButton(null, "NO") {

                    }.show()
            }
        })
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.adapter = mAdapter
    }

}
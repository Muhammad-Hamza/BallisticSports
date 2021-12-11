package com.example.sportsballistics.ui.dashboard.club

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import com.example.sportsballistics.databinding.FragmentClubBinding
import com.example.sportsballistics.ui.dashboard.DashboardActivity
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clubListLayout.llAddClub.setOnClickListener {
            val args = Bundle()
            args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_ADD)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_clubFragment_to_createClubFragment, args)
        }
        binding.clubListLayout.llAddClubAdmin.setOnClickListener {
            val args = Bundle()
            args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_ADD)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_clubFragment_to_createClubAdminFragment, args)
        }
        binding.clubListLayout.etReason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s) && s!!.length >= 3) {
                    getDataFromServer(s.toString())
                } else {
                    if (TextUtils.isEmpty(s))
                        getDataFromServer()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.clubListLayout.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
    }

    private fun initRecyclerView(list: MutableList<UsersItem>) {
        val mLayoutManager = LinearLayoutManager(context)
        val mAdapter = ClubListAdapter(context, list, object : ClubListAdapter.OnItemClickListener {
            override fun onEditClick(adapterType: Int, user: UsersItem) {
                val args = Bundle()
                args.putString(AppConstant.INTENT_EXTRA_1, user.id)
                args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_EDIT)
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_clubFragment_to_createClubFragment, args)
            }

            override fun onViewClick(adapterType: Int, user: UsersItem) {
                val args = Bundle()
                args.putString(AppConstant.INTENT_EXTRA_1, user.id)
                args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_VIEW)
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_clubFragment_to_createClubFragment, args)
            }

            override fun onDeleteClick(adapterType: Int, user: UsersItem) {
                MaterialDialog(binding.root.context)
                    .title(null, "Want to delete!")
                    .message(null, "Do you want to delete this Club?")
                    .positiveButton(null, "YES", {

                    }).negativeButton(null, "NO", {

                    }).show()
            }
        })
        binding.clubListLayout.recyclerView.layoutManager = mLayoutManager
        binding.clubListLayout.recyclerView.adapter = mAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ClubListViewModel::class.java)
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
        getDataFromServer()
    }

    private fun getDataFromServer(strKeyword: String) {
        viewModel.getContent(
            requireContext(),
            URLIdentifiers.CLUB_CONTENT,
            strKeyword,
            object : ClubListViewModel.ContentFetchListener {
                override fun onFetched(content: ClubResponse) {
                    if (content.content != null && content.content.users != null && content.content.users.size > 0)
                        initRecyclerView(content.content?.users as MutableList<UsersItem>)
                    else
                        initRecyclerView(ArrayList<UsersItem>())
                }
            })

    }

    private fun getDataFromServer() {
        viewModel.getContent(
            requireContext(),
            URLIdentifiers.CLUB_CONTENT,
            "",
            object : ClubListViewModel.ContentFetchListener {
                override fun onFetched(content: ClubResponse) {
                    initRecyclerView(content.content?.users as MutableList<UsersItem>)
                }
            })

    }
}
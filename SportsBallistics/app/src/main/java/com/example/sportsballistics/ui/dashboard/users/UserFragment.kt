package com.example.sportsballistics.ui.dashboard.users

import android.os.Bundle
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

    private fun initRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context)
        var mAdapter = ClubListAdapter(context, null, object : ClubListAdapter.OnItemClickListener {
            override fun onEditClick(adapterType: Int, user: UsersItem) {
                TODO("Not yet implemented")
            }

            override fun onViewClick(adapterType: Int, user: UsersItem) {
                TODO("Not yet implemented")
            }

            override fun onDeleteClick(adapterType: Int, user: UsersItem) {
                TODO("Not yet implemented")
            }
        })
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.adapter = mAdapter
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
                //TODO Asher bind data with ui
                Log.d(UserFragment::class.simpleName, Gson().toJson(content))
                initRecyclerView(content.content?.users as MutableList<UsersItem>)
            }
        })
    }

    private fun initRecyclerView(list: MutableList<UsersItem>) {
        val mLayoutManager = LinearLayoutManager(context)
        val mAdapter = UserAdapter(context, list, object :
            UserAdapter.OnItemClickListener {
            override fun onEditClick(adapterType: Int, user: UsersItem) {
                val args = Bundle()
                args.putString(AppConstant.INTENT_EXTRA_1, user.id)
                args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_EDIT)
//                Navigation.findNavController(binding.root)
//                    .navigate(R.id.action_clubFragment_to_createClubFragment, args)
            }

            override fun onViewClick(adapterType: Int, user: UsersItem) {
                val args = Bundle()
                args.putString(AppConstant.INTENT_EXTRA_1, user.id)
                args.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_VIEW)
//                Navigation.findNavController(binding.root)
//                    .navigate(R.id.action_clubFragment_to_createClubFragment, args)
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
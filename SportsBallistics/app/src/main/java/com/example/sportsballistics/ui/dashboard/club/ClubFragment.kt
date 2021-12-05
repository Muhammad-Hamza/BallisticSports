package com.example.sportsballistics.ui.dashboard.club

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
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

    private fun initRecyclerView(list: MutableList<UsersItem>) {
        val mLayoutManager = LinearLayoutManager(context)
        val mAdapter = ClubListAdapter(context, list, object : ClubListAdapter.OnItemClickListener {
            override fun onEditClick(adapterType: Int, user: UsersItem) {
            }

            override fun onViewClick(adapterType: Int, user: UsersItem) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_clubFragment_to_createClubFragment)
//                (activity as DashboardActivity).add(CreateClubFragment(), R.id.flFragment)
            }

            override fun onDeleteClick(adapterType: Int, user: UsersItem) {
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
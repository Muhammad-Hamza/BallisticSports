package com.example.sportsballistics.ui.dashboard.clubs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.FragmentClubBinding

class ClubFragment : Fragment()
{
    lateinit var binding:FragmentClubBinding
    private lateinit var viewModel: ClubListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
       binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_club, container, false);
        initViewModel()

        binding.rlClub.setOnClickListener{
            binding.clubListLayout.clubListLayoutParent.visibility = View.VISIBLE
            binding.llDashboard.visibility = View.GONE
        }
        binding.clubListLayout.backClubList.setOnClickListener{
            binding.clubListLayout.clubListLayoutParent.visibility = View.GONE
            binding.llDashboard.visibility = View.VISIBLE
        }
        return binding.root
    }



    private fun initRecyclerView()
    {
        val mLayoutManager = LinearLayoutManager(context)
        var mAdapter = ClubListAdapter(context, null, object : ClubListAdapter.OnItemClickListener
        {
            override fun onClick(user: UsersItem)
            {

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

        viewModel.getContent(requireContext(),URLIdentifiers.CLUB_CONTENT,"" ,object : ClubListViewModel.ContentFetchListener {
            override fun onFetched(content: ClubResponse) {
//                initRecyclerView(content.content?.users as MutableList<UsersItem>)
                initRecyclerView()
            }
        })
    }
}
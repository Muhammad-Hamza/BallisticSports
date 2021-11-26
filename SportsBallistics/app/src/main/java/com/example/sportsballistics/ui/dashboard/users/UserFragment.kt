package com.example.sportsballistics.ui.dashboard.users

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
import com.example.sportsballistics.databinding.FragmentTrainerBinding
import com.example.sportsballistics.databinding.FragmentUserBinding
import com.example.sportsballistics.ui.dashboard.clubs.ClubListAdapter
import com.example.sportsballistics.ui.dashboard.clubs.ClubListViewModel


class UserFragment : Fragment()
{
    lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: ClubListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user, container, false);
        initViewModel()

        binding.clubListHeader.txtClubName.text = "User Name"
        return binding.root
    }



    private fun initRecyclerView()
    {
        val mLayoutManager = LinearLayoutManager(context)
        var mAdapter = ClubListAdapter(context, null, object : ClubListAdapter.OnItemClickListener
        {
            override fun onEditClick(adapterType: Int, user: UsersItem)
            {
                TODO("Not yet implemented")
            }

            override fun onViewClick(adapterType: Int, user: UsersItem)
            {
                TODO("Not yet implemented")
            }

            override fun onDeleteClick(adapterType: Int, user: UsersItem)
            {
                TODO("Not yet implemented")
            }
        })
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.adapter = mAdapter
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

        viewModel.getContent(requireContext(), URLIdentifiers.CLUB_CONTENT,"" ,object : ClubListViewModel.ContentFetchListener {
            override fun onFetched(content: ClubResponse) {
//                initRecyclerView(content.content?.users as MutableList<UsersItem>)
                initRecyclerView()
            }
        })
    }
}
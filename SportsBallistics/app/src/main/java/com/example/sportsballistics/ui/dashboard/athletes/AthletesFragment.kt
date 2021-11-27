package com.example.sportsballistics.ui.dashboard.athletes

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.appInterface.OnItemClickListener
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.FragmentAthletesBinding
import com.example.sportsballistics.databinding.FragmentClubBinding
import com.example.sportsballistics.ui.dashboard.clubs.ClubListAdapter
import com.example.sportsballistics.ui.dashboard.clubs.ClubListViewModel
import com.example.sportsballistics.utils.AppFunctions
import com.example.sportsballistics.utils.DummyContent

class AthletesFragment : Fragment() {
    lateinit var binding: FragmentAthletesBinding
    private lateinit var viewModel: ClubListViewModel
    private lateinit var adapter: AthletesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_athletes, container, false);
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clubListLayout.tvClub.setText(
            AppFunctions.getSpannableText(
                getString(R.string.txt_athletes_club_name),
                "{{clubName}}",
                "Club Name 1"
            )
        )
        binding.clubListLayout.tvTrainer.setText(
            AppFunctions.getSpannableText(
                getString(R.string.txt_athletes_trainer),
                "{{trainer}}",
                "Mike Thomas"
            )
        )
        binding.clubListLayout.tvAge.setText(
            AppFunctions.getSpannableText(
                getString(R.string.txt_athletes_age),
                "{{age}}",
                "25"
            )
        )
        binding.clubListLayout.tvGrade.setText(
            AppFunctions.getSpannableText(
                getString(R.string.txt_athletes_grade),
                "{{grade}}",
                "10"
            )
        )
        val strContent = getString(R.string.txt_atheleteNameStr).replace("{{name}}", "John Smith")
        binding.clubListLayout.tvAdditionalInfo.setText(AppFunctions.getSpannableText(strContent))
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.clubListLayout.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.clubListLayout.recyclerView.setHasFixedSize(true)
        adapter = AthletesAdapter(DummyContent.loadDummyContentForAthletes(),
            object : OnItemClickListener {
                override fun onEditClick(adapterType: Int, anyData: Any) {

                }

                override fun onViewClick(adapterType: Int, anyData: Any) {
                }

                override fun onDeleteClick(adapterType: Int, anyData: Any) {
                }

            })
        binding.clubListLayout.recyclerView.adapter = adapter
    }

    fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ClubListViewModel::class.java)
//        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener {
//            override fun dismissDialog() {
//            }
//
//            override fun addDialog() {
//            }
//
//            override fun addErrorDialog() {
//            }
//
//            override fun addErrorDialog(msg: String?) {
//            }
//        })
//
//        viewModel.getContent(requireContext(), URLIdentifiers.ATHLETE_CONTENT, "", object :
//            ClubListViewModel.ContentFetchListener {
//            override fun onFetched(content: ClubResponse) {
////                initRecyclerView(content.content?.users as MutableList<UsersItem>)
//                initRecyclerView()
//            }
//        })
    }
}

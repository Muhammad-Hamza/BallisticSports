package com.example.sportsballistics.ui.dashboard.athletes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.appInterface.OnItemClickListener
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.local.AthletesModel
import com.example.sportsballistics.data.local.LookupModel
import com.example.sportsballistics.data.remote.generic.GenericResponse
import com.example.sportsballistics.data.remote.generic.UserModel
import com.example.sportsballistics.databinding.FragmentAthletesBinding
import com.example.sportsballistics.utils.AppFunctions
import com.example.sportsballistics.utils.DummyContent
import com.example.sportsballistics.utils.chart.ChartPerentageFormatter
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class AthletesFragment : Fragment() {
    lateinit var binding: FragmentAthletesBinding
    private lateinit var viewModel: AthletesViewModel
    private lateinit var adapter: AthletesAdapter
    private var currentModel: AthletesModel? = null
    private var coachListAdapter: CoachDataAdapter? = null
    private var athletesAdapter: AthletesUserAdapter? = null

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
        binding.clubListLayout.llList.visibility = View.VISIBLE
        binding.clubListLayout.llMainLayout.visibility = View.GONE
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
        loadMainUI()
        initRecyclerView()
        binding.clubListLayout.tvDashboard.setOnClickListener {
            if (currentModel != null && binding.clubListLayout.recyclerView.visibility == View.GONE) {
                currentModel = null
                loadMainUI()
            }
        }
        binding.clubListLayout.tvNext.setOnClickListener {
            if (currentModel != null && binding.clubListLayout.recyclerView.visibility == View.GONE) {
                Toast.makeText(requireContext(), "onNext Click", Toast.LENGTH_SHORT).show()
            }
        }
        loadCoachabilityChart()
        initCoachListData()
    }

    private fun initCoachListData() {
        binding.clubListLayout.rvCoachList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.clubListLayout.rvCoachList.setHasFixedSize(true)
        val list = ArrayList<LookupModel>()
        list.add(LookupModel(0, "Listening Skills", "22"))
        list.add(LookupModel(1, "Following Direction", "28"))
        list.add(LookupModel(2, "Attitude", "8"))
        list.add(LookupModel(3, "Focus", "18"))
        list.add(LookupModel(4, "Work Ethics", "24"))
        coachListAdapter = CoachDataAdapter(list)
        binding.clubListLayout.rvCoachList.adapter = coachListAdapter
        binding.clubListLayout.tvSummary.setText("AVG: 8 | SUM: 48")

    }

    private fun loadMainUI() {
        binding.clubListLayout.recyclerView.visibility = View.VISIBLE
        binding.clubListLayout.rlCoach.visibility = View.GONE
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
                    if (anyData is AthletesModel) {
                        if (anyData.id == 0) {
                            loadCoachData(anyData)
                        }
                    }
                }

                override fun onDeleteClick(adapterType: Int, anyData: Any) {
                }

            })
        binding.clubListLayout.recyclerView.adapter = adapter
    }

    private fun loadCoachData(model: AthletesModel) {
        this.currentModel = model
        binding.clubListLayout.recyclerView.visibility = View.GONE
        binding.clubListLayout.txtDetailHeading.setText(model.heading)
        binding.clubListLayout.rlCoach.visibility = View.VISIBLE
    }

    fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AthletesViewModel::class.java)
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
//
        viewModel.getContent(requireContext(), URLIdentifiers.ATHLETE_CONTENT, "", object :
            AthletesViewModel.ContentFetchListener {
            override fun onFetched(content: GenericResponse) {
                binding.progressBar.visibility = View.GONE
                if (content.content!!.users != null && content.content!!.users.size > 0) {
                    initAtheletesRecyclerView(content.content!!.users)
                } else {
                    showMessage("No Atheletes found")
                }
            }
        })
    }

    private fun initAtheletesRecyclerView(mutableList: ArrayList<UserModel>) {
        //TODO:: HAmza bhai load listItem here.
//        binding.clubListLayout.rvAtheletes
//        this recyclerView is using for athelets lists
//        -------------------------------------
        athletesAdapter = AthletesUserAdapter(object : OnItemClickListener {
            override fun onEditClick(adapterType: Int, anyData: Any) {

            }

            override fun onViewClick(adapterType: Int, anyData: Any) {
                bindDataInUserProfile(anyData as UserModel)
            }

            override fun onDeleteClick(adapterType: Int, anyData: Any) {

            }

        })
//        -------------------------------------
        binding.clubListLayout.llList.visibility = View.VISIBLE
        binding.clubListLayout.llMainLayout.visibility = View.GONE
        binding.clubListLayout.rvAtheletes.setHasFixedSize(true)
        binding.clubListLayout.rvAtheletes.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.clubListLayout.rvAtheletes.adapter = athletesAdapter
        athletesAdapter!!.loadData(mutableList)

    }

    private fun bindDataInUserProfile(userModel: UserModel) {
        binding.clubListLayout.llList.visibility = View.GONE
        binding.clubListLayout.llMainLayout.visibility = View.VISIBLE
    }

    private fun loadCoachabilityChart() {
        binding.clubListLayout.pieChart.setUsePercentValues(true);

        val yvalues: ArrayList<PieEntry> = ArrayList<PieEntry>();
        yvalues.add(PieEntry(22f, "Listening Skills", 0))
        yvalues.add(PieEntry(28f, "Following Direction", 1))
        yvalues.add(PieEntry(8f, "Attitude", 2))
        yvalues.add(PieEntry(18f, "Focus", 3))
        yvalues.add(PieEntry(24f, "Work Ethics", 4))
//        yvalues.add(PieEntry(22f, "Listening Skills", "22 %"))
//        yvalues.add(PieEntry(28f, "Following Direction", "28 %"))
//        yvalues.add(PieEntry(8f, "Attitude", "8 %"))
//        yvalues.add(PieEntry(18f, "Focus", "18 %"))
//        yvalues.add(PieEntry(24f, "Work Ethics", "24 %"))

        val dataSet = PieDataSet(yvalues, "");
        dataSet.setDrawValues(true)
        val colorList = ArrayList<Int>()
        colorList.add(Color.WHITE)
        dataSet.setValueTextColors(colorList)
        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueFormatter(ChartPerentageFormatter());
        binding.clubListLayout.pieChart.setData(data);
        val description = Description();
        description.setText("");
        binding.clubListLayout.pieChart.setDrawEntryLabels(false)//drawEntryLabelsEnabled = true

//        binding.clubListLayout.pieChart.setdraw(false)
//        binding.clubListLayout.pieChart.setcolor
        binding.clubListLayout.pieChart.setDescription(description);
        binding.clubListLayout.pieChart.setDrawHoleEnabled(true);
        binding.clubListLayout.pieChart.setTransparentCircleRadius(58f);
        binding.clubListLayout.pieChart.setHoleRadius(58f);
//        binding.clubListLayout.pieChart.setEntryLabelColor(
//            ContextCompat.getColor(
//                requireContext(),
//                R.color.white
//            )
//        )
        binding.clubListLayout.pieChart.setEntryLabelColor(Color.WHITE)
        val listOfColors = ArrayList<Int>()
        listOfColors.add(ContextCompat.getColor(requireContext(), R.color.txt_color_listening))
        listOfColors.add(
            ContextCompat.getColor(
                requireContext(),
                R.color.txt_color_following_direction
            )
        )
        listOfColors.add(ContextCompat.getColor(requireContext(), R.color.txt_color_attitude))
        listOfColors.add(ContextCompat.getColor(requireContext(), R.color.txt_color_focus))
        listOfColors.add(ContextCompat.getColor(requireContext(), R.color.txt_color_work_ethics))
        dataSet.setColors(listOfColors);
        data.setValueTextSize(10f);

        val l: Legend = binding.clubListLayout.pieChart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        l.textSize = 9f
        l.yOffset = 5f
    }

    private fun showMessage(content: String) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
    }
}

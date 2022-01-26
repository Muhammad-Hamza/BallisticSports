package com.example.sportsballistics.ui.dashboard.athletes

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.appInterface.OnItemClickListener
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.athletes.AthleteDataModel
import com.example.sportsballistics.data.remote.athletes.Service
import com.example.sportsballistics.data.remote.generic.GenericResponse
import com.example.sportsballistics.data.remote.generic.UserModel
import com.example.sportsballistics.databinding.FragmentAthletesBinding
import com.example.sportsballistics.utils.AppConstant
import com.example.sportsballistics.utils.AppFunctions
import com.example.sportsballistics.utils.chart.ChartPerentageFormatter
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_create_club.*

class AthletesFragment : Fragment() {
    lateinit var binding: FragmentAthletesBinding
    private lateinit var viewModel: AthletesViewModel
    private lateinit var adapter: AthletesAdapter
    private var currentModel: List<Service>? = null
    private var coachListAdapter: CoachDataAdapter? = null
    private var athletesAdapter: AthletesUserAdapter? = null
    private var currentAthleteDataModel: AthleteDataModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_athletes, container, false);
        loadAssets()
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clubListLayout.llList.visibility = View.VISIBLE
        binding.clubListLayout.llMainLayout.visibility = View.GONE
        loadMainUI()
        binding.clubListLayout.llEditAthlete.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(
                AppConstant.INTENT_EXTRA_1,
                currentAthleteDataModel!!.user_id
            )
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_athletesFragment_to_formListFragment, bundle)
        }

        binding.clubListLayout.etReason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s)) {
                    loadDataFromServer(s.toString())
                } else {
                    if (TextUtils.isEmpty(s))
                        loadDataFromServer("")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.clubListLayout.tvDashboard.setOnClickListener()
        {
            if (currentModel != null && binding.clubListLayout.recyclerView.visibility == View.GONE) {
                currentModel = null
                loadMainUI()
            }
        }
        binding.clubListLayout.tvNext.setOnClickListener()
        {
            if (currentModel != null && binding.clubListLayout.recyclerView.visibility == View.GONE) {
//                Toast.makeText(binding.root.context, "onNext Click", Toast.LENGTH_SHORT).show()
                if (binding.clubListLayout.pieChart.visibility == View.VISIBLE) {
                    binding.clubListLayout.pieChart.visibility = View.GONE
                    binding.clubListLayout.barChart.visibility = View.VISIBLE
                } else {
                    binding.clubListLayout.pieChart.visibility = View.VISIBLE
                    binding.clubListLayout.barChart.visibility = View.GONE
                }
            }
        }
        binding.clubListLayout.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.clubListLayout.llAddAthlete.setOnClickListener()
        {
            val bundle = Bundle()
            bundle.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_ADD)

            Navigation.findNavController(binding.root)
                .navigate(R.id.action_athletesFragment_to_createAthleteFragment, bundle)
        }
        binding.clubListLayout.llAddTrainer.setOnClickListener()
        {
            val bundle = Bundle()
            bundle.putInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_ADD)

            Navigation.findNavController(binding.root)
                .navigate(R.id.action_athletesFragment_to_createTrainerFragment, bundle)
        }
        when (AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId) {
            AppConstant.ROLE_CLUB_PORTAL -> {
                binding.clubListLayout.llAddAthlete.visibility = View.VISIBLE
                binding.clubListLayout.llAddTrainer.visibility = View.VISIBLE
            }
            AppConstant.ROLE_SUPER_PORTAL -> {
                binding.clubListLayout.llAddAthlete.visibility = View.GONE
                binding.clubListLayout.llAddTrainer.visibility = View.GONE
            }
            AppConstant.ROLE_ATHLETES_PORTAL -> {
                binding.clubListLayout.llAddAthlete.visibility = View.GONE
                binding.clubListLayout.llAddTrainer.visibility = View.GONE
            }
            AppConstant.ROLE_TRAINER_PORTAL -> {
                binding.clubListLayout.llAddAthlete.visibility = View.VISIBLE
                binding.clubListLayout.llAddTrainer.visibility = View.GONE
            }
            else -> {
                binding.clubListLayout.llAddAthlete.visibility = View.GONE
                binding.clubListLayout.llAddTrainer.visibility = View.GONE
            }
        }

    }

    private fun initCoachListData(services: List<Service>) {
        binding.clubListLayout.rvCoachList.layoutManager =
            LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        binding.clubListLayout.rvCoachList.setHasFixedSize(true)
        coachListAdapter = CoachDataAdapter(services)
        binding.clubListLayout.rvCoachList.adapter = coachListAdapter
        var sum = 0
        var avg = 0
        for (i in 0..(services.size - 1)) {
            sum = sum.toInt() + services.get(i).sum.toInt()
            avg = avg.toInt() + services.get(i).average.toInt()
        }
        binding.clubListLayout.tvSummary.setText("AVG: ${avg} | SUM: ${sum}")

    }

    private fun loadMainUI() {
        binding.clubListLayout.recyclerView.visibility = View.VISIBLE
        binding.clubListLayout.rlCoach.visibility = View.GONE
    }

    //TODO whats this?
    private fun initRecyclerView(services: List<Service>) {
        binding.clubListLayout.recyclerView.layoutManager =
            GridLayoutManager(binding.root.context, 2, RecyclerView.VERTICAL, false)
        binding.clubListLayout.recyclerView.setHasFixedSize(true)
        adapter = AthletesAdapter(services,
            object : OnItemClickListener {
                override fun onEditClick(adapterType: Int, anyData: Any) {

                }

                override fun onViewClick(adapterType: Int, anyData: Any) {
                    if (anyData is List<*>) {
                        loadCoachData(anyData as List<Service>, adapterType)
                    }
                }

                override fun onDeleteClick(adapterType: Int, id: String) {

                }

                override fun onDashboardClick(adapterType: Int, anyData: Any) {
                }
            })
        binding.clubListLayout.recyclerView.adapter = adapter
    }

    private fun loadCoachData(model: List<Service>, adapterType: Int) {
        this.currentModel = model
        binding.clubListLayout.recyclerView.visibility = View.GONE
        binding.clubListLayout.txtDetailHeading.setText(model.get(adapterType).name)
        binding.clubListLayout.rlCoach.visibility = View.VISIBLE
        binding.clubListLayout.pieChart.visibility = View.VISIBLE
        binding.clubListLayout.barChart.visibility = View.GONE
    }

    private fun initViewModel() {
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

        loadDataFromServer("")
//
    }

    private fun loadDataFromServer(strKeywords: String) {
        viewModel.getContent(
            binding.root.context,
            URLIdentifiers.ATHLETE_CONTENT,
            strKeywords,
            object :
                AthletesViewModel.ContentFetchListener {
                override fun onFetched(content: Any) {
                    binding.progressBar.visibility = View.GONE
                    if (content != null && content is GenericResponse) {
                        if (content.content != null && content.content!!.users != null && content.content.users.size > 0) {
                            initAtheletesRecyclerView(content.content.users)
                        } else {
                            initAtheletesRecyclerView(ArrayList<UserModel>())
                            showMessage("No Athletes found")
                        }
                    }
                }
            })
    }

    private fun initAtheletesRecyclerView(mutableList: ArrayList<UserModel>) {
        //TODO:: HAmza bhai load listItem here.
//        binding.clubListLayout.rvAtheletes
//        this recyclerView is using for athelets lists
//        -------------------------------------
        athletesAdapter =
            AthletesUserAdapter(AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId!!,
                object : OnItemClickListener {
                    override fun onEditClick(adapterType: Int, anyData: Any) {
                        if (anyData is UserModel) {
                            val bundle = Bundle()
                            bundle.putInt(
                                AppConstant.INTENT_SCREEN_TYPE,
                                AppConstant.INTENT_SCREEN_TYPE_EDIT
                            )
                            bundle.putString(AppConstant.INTENT_EXTRA_1, anyData.id)

                            Navigation.findNavController(binding.root)
                                .navigate(
                                    R.id.action_athletesFragment_to_createAthleteFragment,
                                    bundle
                                )
                        }
                    }

                    override fun onDashboardClick(adapterType: Int, anyData: Any) {
                        viewModel.getAthleteInfo(
                            binding.root.context,
                            anyData as UserModel,
                            object : AthletesViewModel.ContentFetchListener {
                                override fun onFetched(anyObject: Any) {
                                    if (anyObject is DashboardModel) {
                                        bindDataInUserProfile(anyObject.data as AthleteDataModel)
                                    }
                                }

                            });
                    }

                    override fun onViewClick(adapterType: Int, anyData: Any) {
                        if (anyData is UserModel) {
                            val bundle = Bundle()
                            bundle.putInt(
                                AppConstant.INTENT_SCREEN_TYPE,
                                AppConstant.INTENT_SCREEN_TYPE_VIEW
                            )
                            bundle.putString(AppConstant.INTENT_EXTRA_1, anyData.id)

                            Navigation.findNavController(binding.root)
                                .navigate(
                                    R.id.action_athletesFragment_to_createAthleteFragment,
                                    bundle
                                )
                        }
//                bindDataInUserProfile(anyData as UserModel)
                    }

                    override fun onDeleteClick(adapterType: Int, id: String) {
                        MaterialDialog(binding.root.context)
                            .title(null, "Want to delete!")
                            .message(null, "Do you want to delete this user?")
                            .positiveButton(null, "YES") {
                                viewModel.deleteTrainer(binding.root.context, id, object :
                                    AthletesViewModel.ContentFetchListener {
                                    override fun onFetched(anyObject: Any) {
                                        showMessage("Athlete Deleted")
                                    }
                                })
                            }.negativeButton(null, "NO") {

                            }.show()
                    }

                })
//        -------------------------------------
        binding.clubListLayout.llList.visibility = View.VISIBLE
        binding.clubListLayout.llMainLayout.visibility = View.GONE
        binding.clubListLayout.rvAtheletes.setHasFixedSize(true)
        binding.clubListLayout.rvAtheletes.layoutManager =
            LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        binding.clubListLayout.rvAtheletes.adapter = athletesAdapter
        athletesAdapter!!.loadData(mutableList)

    }

    private fun bindDataInUserProfile(athleteDataModel: AthleteDataModel) {
        this.currentAthleteDataModel = athleteDataModel
        if(athleteDataModel.clubname != null)
        {
            binding.clubListLayout.tvClub.setText(AppFunctions.getSpannableText(getString(R.string.txt_athletes_club_name), "{{clubName}}", athleteDataModel.clubname))
        }
//        binding.clubListLayout.tvTrainer.setText(
//            AppFunctions.getSpannableText(
//                getString(R.string.txt_athletes_trainer),
//                "{{trainer}}",
//                athleteDataModel.
//            )
//        )
        binding.clubListLayout.tvAge.setText(
            AppFunctions.getSpannableText(
                getString(R.string.txt_athletes_age),
                "{{age}}",
                athleteDataModel.athletic_name.age
            )
        )
        binding.clubListLayout.tvGrade.setText(
            AppFunctions.getSpannableText(
                getString(R.string.txt_athletes_grade),
                "{{grade}}",
                athleteDataModel.athletic_name.grade
            )
        )
        var dataContent = getString(R.string.txt_atheleteNameStr).replace(
            "{{name}}", athleteDataModel.athletic_name.fullname
        )
        binding.clubListLayout.tvName.setText(athleteDataModel.athletic_name.fullname)
        when (SharedPrefUtil.getInstance().sportsType) {
            AppConstant.BASEBALL -> {
                dataContent = dataContent.replace("{{COLOR}}", "E86A24")
            }
            AppConstant.VOLLEYBALL -> {
                dataContent = dataContent.replace("{{COLOR}}", "DE9700")
            }
            AppConstant.QB -> {
                dataContent = dataContent.replace("{{COLOR}}", "C5203D")
            }
            else -> {
                dataContent = dataContent.replace("{{COLOR}}", "521A71")
            }
        }

        Glide.with(binding.root.context).load(athleteDataModel.profile_image)
            .into(binding.clubListLayout.ivImageView)
        binding.clubListLayout.tvAdditionalInfo.setText(AppFunctions.getSpannableText(dataContent))

        loadCoachabilityChart(athleteDataModel.services)
        initCoachListData(athleteDataModel.services)
        initRecyclerView(athleteDataModel.services)
        binding.clubListLayout.llList.visibility = View.GONE
        binding.clubListLayout.llMainLayout.visibility = View.VISIBLE
        when (AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId) {
            AppConstant.ROLE_CLUB_PORTAL -> {
                binding.clubListLayout.llEditAthlete.visibility = View.VISIBLE
            }
            AppConstant.ROLE_SUPER_PORTAL -> {

            }
            AppConstant.ROLE_ATHLETES_PORTAL -> {

            }
            AppConstant.ROLE_TRAINER_PORTAL -> {

            }
            else -> {
                binding.clubListLayout.llEditAthlete.visibility = View.GONE
            }
        }
    }

    private fun loadCoachabilityChart(services: List<Service>) {
        binding.clubListLayout.pieChart.setUsePercentValues(true);

        val values = ArrayList<BarEntry>()
        val yvalues: ArrayList<PieEntry> = ArrayList<PieEntry>();
        for (i in 0..(services.size - 1)) {
            yvalues.add(PieEntry(services.get(i).average.toFloat(), services.get(i).name, (i + 1)))
            values.add(
                BarEntry(
                    (i + 1).toFloat(),
                    services.get(i).average.toFloat(),
                    services.get(i).name
                )
            )
        }
        val dataSet = PieDataSet(yvalues, "");
        dataSet.setDrawValues(true)
//        val colorList = ArrayList<Int>()
//        colorList.add(Color.WHITE)
//        dataSet.setValueTextColors(colorList)
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
//                binding.root.context,
//                R.color.white
//            )
//        )
        binding.clubListLayout.pieChart.setEntryLabelColor(Color.WHITE)
        val listOfColors = ArrayList<Int>()
        listOfColors.add(ContextCompat.getColor(binding.root.context, R.color.txt_color_listening))
        listOfColors.add(
            ContextCompat.getColor(
                binding.root.context,
                R.color.txt_color_following_direction
            )
        )
        listOfColors.add(ContextCompat.getColor(binding.root.context, R.color.txt_color_attitude))
        listOfColors.add(ContextCompat.getColor(binding.root.context, R.color.txt_color_focus))
        listOfColors.add(
            ContextCompat.getColor(
                binding.root.context,
                R.color.txt_color_work_ethics
            )
        )
//        dataSet.setColors(listOfColors);
        dataSet.setColors(ColorTemplate.createColors(ColorTemplate.COLORFUL_COLORS));
        data.setValueTextSize(10f);

        val l: Legend = binding.clubListLayout.pieChart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        l.textSize = 9f
        l.yOffset = 5f

        //This is for a barChart
        binding.clubListLayout.barChart.getDescription().setEnabled(true)
        binding.clubListLayout.barChart.setPinchZoom(false)
//
        binding.clubListLayout.barChart.setDrawBarShadow(false)
        binding.clubListLayout.barChart.setDrawGridBackground(false)

        binding.clubListLayout.barChart.setDrawValueAboveBar(true)
        val xAxis: XAxis = binding.clubListLayout.barChart.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        binding.clubListLayout.barChart.getAxisLeft().setDrawGridLines(false)

        // add a nice and smooth animation
        binding.clubListLayout.barChart.animateY(1500)

        binding.clubListLayout.barChart.getLegend().setEnabled(true)
        binding.clubListLayout.barChart.legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT

        val set1: BarDataSet

        set1 = BarDataSet(values, "Data Set")
//        set1.setColors(listOfColors)
        dataSet.setColors(ColorTemplate.createColors(ColorTemplate.VORDIPLOM_COLORS));
        set1.setDrawValues(false)
        val dataSets = java.util.ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val newBarData = BarData(dataSets)

        binding.clubListLayout.barChart.description.isEnabled = true // hide the description
        binding.clubListLayout.barChart.legend.isEnabled = true // hide the legend

        binding.clubListLayout.barChart.xAxis.setDrawLabels(true) // hide bottom label
        binding.clubListLayout.barChart.axisLeft.setDrawLabels(true) // hide left label
        binding.clubListLayout.barChart.axisRight.setDrawLabels(false) // hide right label

        binding.clubListLayout.barChart.setData(newBarData)
        binding.clubListLayout.barChart.setFitBars(true)

        binding.clubListLayout.barChart.invalidate()


//        binding.clubListLayout.pieChart.visibility = View.VISIBLE
//        binding.clubListLayout.barChart.visibility = View.GONE
    }

    private fun showMessage(content: String) {
        Toast.makeText(binding.root.context, content, Toast.LENGTH_SHORT).show()
    }

    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType

        AppConstant.changeColor(binding.clubListLayout.txtTotalTrainersText)
        AppConstant.changeColor(binding.clubListLayout.clubListHeader.txtSerialNo)
        AppConstant.changeColor(binding.clubListLayout.clubListHeader.txtClubName)
        AppConstant.changeColor(binding.clubListLayout.clubListHeader.txtAction)
        AppConstant.changeColor(binding.clubListLayout.tvName)
        AppConstant.changeColor(binding.clubListLayout.tvClub)
        AppConstant.changeColor(binding.clubListLayout.tvAge)
        AppConstant.changeColor(binding.clubListLayout.tvTrainer)
        AppConstant.changeColor(binding.clubListLayout.tvGrade)
        AppConstant.changeColor(binding.clubListLayout.txtDetailHeading)
        AppConstant.changeColor(binding.clubListLayout.tvSummary)
        AppConstant.changeColor(binding.clubListLayout.tvEdit)

        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg)
                    .into(binding.clubListLayout.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo)
                    .into(binding.clubListLayout.imgLogo)
                binding.clubListLayout.llProfileLayout.setBackgroundResource(R.drawable.ic_bb_dash_profile)
            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg)
                    .into(binding.clubListLayout.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo)
                    .into(binding.clubListLayout.imgLogo)
                binding.clubListLayout.llProfileLayout.setBackgroundResource(R.drawable.ic_vb_dash_profile)
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.clubListLayout.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.clubListLayout.imgLogo)
                binding.clubListLayout.llProfileLayout.setBackgroundResource(R.drawable.ic_dash_profile)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg)
                    .into(binding.clubListLayout.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo)
                    .into(binding.clubListLayout.imgLogo)
                binding.clubListLayout.llProfileLayout.setBackgroundResource(R.drawable.ic_qb_dash_profile)
            }
        }
    }

//    fun loadAssets() {
//        val sportsType = SharedPrefUtil.getInstance().sportsType
//
//
//        when (sportsType) {
//            AppConstant.BASEBALL -> {
//                Glide.with(binding.root).load(R.drawable.bb_login_bg)
//                    .into(binding.clubListLayout.ivBackground)
//                Glide.with(binding.root).load(R.drawable.bb_inner_logo)
//                    .into(binding.clubListLayout.imgLogo)
//            }
//            AppConstant.VOLLEYBALL -> {
//                Glide.with(binding.root).load(R.drawable.vb_login_bg)
//                    .into(binding.clubListLayout.ivBackground)
//                Glide.with(binding.root).load(R.drawable.vb_inner_logo)
//                    .into(binding.clubListLayout.imgLogo)
//            }
//            AppConstant.TODDLER -> {
//                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
//                    .into(binding.clubListLayout.ivBackground)
//                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
//                    .into(binding.clubListLayout.imgLogo)
//            }
//            AppConstant.QB -> {
//                Glide.with(binding.root).load(R.drawable.qb_login_bg)
//                    .into(binding.clubListLayout.ivBackground)
//                Glide.with(binding.root).load(R.drawable.qb_inner_logo)
//                    .into(binding.clubListLayout.imgLogo)
//            }
//        }
//    }

}

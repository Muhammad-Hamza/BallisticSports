package com.example.sportsballistics.ui.dashboard.athletes

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
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
import com.example.sportsballistics.data.remote.form_service.FormServiceModel
import com.example.sportsballistics.data.remote.generic.GenericResponse
import com.example.sportsballistics.data.remote.generic.UserModel
import com.example.sportsballistics.databinding.FragmentAthletesBinding
import com.example.sportsballistics.utils.AppConstant
import com.example.sportsballistics.utils.AppFunctions
import com.example.sportsballistics.utils.AppUtils.Companion.showToast


class AthletesFragment : Fragment() {
    lateinit var binding: FragmentAthletesBinding
    private lateinit var viewModel: AthletesViewModel
    private lateinit var adapter: AthletesAdapter
    private var currentModel: FormServiceModel? = null
    private var coachListAdapter: CoachDataAdapter? = null
    private var athletesAdapter: AthletesUserAdapter? = null
    private var currentAthleteDataModel: AthleteDataModel? = null
    lateinit var pie: Pie;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

    private fun initChart() {
        pie = AnyChart.pie()
        pie.labels().position("inside")

        pie.legend().title().enabled(false)
        pie.legend().paginator(false)
        pie.legend()
            .fontSize(9)
            .position("bottom")
            .iconSize(9)
            .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
            .align(Align.CENTER)
        pie.container("container")
        pie.background().fill("#FFFFFF", 1)
        pie.animation(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChart()
        binding.clubListLayout.llList.visibility = View.VISIBLE
        binding.clubListLayout.llMainLayout.visibility = View.GONE
//        binding.clubListLayout.tvNext.setText("Next")
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

        binding.clubListLayout.tvPrevious.setOnClickListener()
        {
            var adapterPosition = 0
            if (coachListAdapter != null) {
                adapterPosition = coachListAdapter!!.parentPosition
            }
            if ((adapterPosition - 1) == 0) {
                binding.clubListLayout.tvPrevious.visibility = View.INVISIBLE
            } else {
                binding.clubListLayout.tvPrevious.visibility = View.VISIBLE
            }
            binding.clubListLayout.tvNext.visibility = View.VISIBLE
            coachListAdapter!!.parentPosition = adapterPosition - 1
            loadServiceData(
                adapter.list.get(adapterPosition).slug,
                currentAthleteDataModel!!.user_id,
                coachListAdapter!!.parentPosition
            )
//            if (currentModel != null && binding.clubListLayout.recyclerView.visibility == View.GONE) {
//                currentModel = null
//                loadMainUI()
//            }
        }
        binding.clubListLayout.tvNext.setOnClickListener()
        {
            var adapterPosition = 0
            if (coachListAdapter != null) {
                adapterPosition = coachListAdapter!!.parentPosition
            }
            if ((adapterPosition + 1) == (adapter.list.size - 1)) {
                binding.clubListLayout.tvNext.visibility = View.INVISIBLE
            } else {
                binding.clubListLayout.tvNext.visibility = View.VISIBLE
            }
            binding.clubListLayout.tvPrevious.visibility = View.VISIBLE
            coachListAdapter!!.parentPosition = adapterPosition + 1
            loadServiceData(
                adapter.list.get(adapterPosition).slug,
                currentAthleteDataModel!!.user_id,
                coachListAdapter!!.parentPosition
            )

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

    private fun initCoachListData(services: AthleteDataModel, adapterPosition: Int) {
        binding.clubListLayout.rvCoachList.layoutManager =
            LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        binding.clubListLayout.rvCoachList.setHasFixedSize(true)
        coachListAdapter = CoachDataAdapter(services, adapterPosition)
        binding.clubListLayout.rvCoachList.adapter = coachListAdapter
//        var sum = 0
//        var avg = 0
//        for (i in 0..(services.size - 1)) {
//            sum = sum.toInt() + services.get(i).sum.toInt()
//            avg = avg.toInt() + services.get(i).average.toInt()
//        }
        binding.clubListLayout.tvSummary.setText(
            "AVG: ${
                services.average.toInt().toString()
            } | SUM: ${services.sum.toInt().toString()}"
        )

    }

    private fun loadMainUI() {
        binding.clubListLayout.recyclerView.visibility = View.VISIBLE
        binding.clubListLayout.rlCoach.visibility = View.GONE
    }

    //TODO whats this?
    private fun initRecyclerView(services: List<Service>, userId: String) {
        binding.clubListLayout.recyclerView.layoutManager =
            GridLayoutManager(binding.root.context, 2, RecyclerView.VERTICAL, false)
        binding.clubListLayout.recyclerView.setHasFixedSize(true)
        adapter = AthletesAdapter(services,
            object : OnItemClickListener {
                override fun onEditClick(adapterType: Int, anyData: Any) {

                }

                override fun onViewClick(adapterType: Int, anyData: Any) {
                    if (anyData is Service) {
                        loadServiceData(anyData.slug, userId, adapterType)
                    }
                }

                override fun onDeleteClick(adapterType: Int, id: String) {

                }

                override fun onDashboardClick(adapterType: Int, anyData: Any) {
                }
            })
        binding.clubListLayout.recyclerView.adapter = adapter
    }

    private fun loadCoachData(model: FormServiceModel, adapterType: Int) {
        if (adapterType == 0) {
            binding.clubListLayout.tvPrevious.visibility = View.INVISIBLE
        } else {
            binding.clubListLayout.tvPrevious.visibility = View.VISIBLE
        }
        if (adapterType == (adapter.list.size - 1)) {
            binding.clubListLayout.tvNext.visibility = View.INVISIBLE
        } else {
            binding.clubListLayout.tvNext.visibility = View.VISIBLE
        }
        this.currentModel = model
        loadCoachabilityChart(model.data!!)
        initCoachListData(model.data, adapterType)
        binding.clubListLayout.recyclerView.visibility = View.GONE
        binding.clubListLayout.txtDetailHeading.setText(model.data!!.title)
        binding.clubListLayout.rlCoach.visibility = View.VISIBLE
        binding.clubListLayout.pieChart.visibility = View.VISIBLE
//        binding.clubListLayout.barChart.visibility = View.GONE
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AthletesViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogAthleteInteractionListener {
            var loadingDialog: AlertDialog? = null

            init {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setCancelable(false) // if you want user to wait for some process to finish,

                builder.setView(R.layout.layout_loading_dialog)
                builder.setCancelable(false)
                loadingDialog = builder.create()
            }

            override fun dismissDialog() {
                binding.progressBar.visibility = View.GONE
            }

            override fun addDialog() {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun addErrorDialog() {
                binding.progressBar.visibility = View.GONE
                initAtheletesRecyclerView(ArrayList())
            }

            override fun addLoadingDialog() {
                if (loadingDialog != null) {
                    loadingDialog!!.show()
                }
            }

            override fun dismissLoadingDialog() {
                if (loadingDialog != null) {
                    loadingDialog!!.dismiss()
                }
            }

            override fun addErrorDialog(msg: String?) {
                binding.progressBar.visibility = View.GONE
                initAtheletesRecyclerView(ArrayList())
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
                            showToast(R.string.txt_no_athlete_found)
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
                                        showToast(R.string.txt_athlete_deleted)
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
        if (athleteDataModel.clubname != null) {
            binding.clubListLayout.tvClub.setText(
                AppFunctions.getSpannableText(
                    getString(R.string.txt_athletes_club_name),
                    "{{clubName}}",
                    athleteDataModel.clubname
                )
            )
        }
//        binding.clubListLayout.tvTrainer.setText(
//            AppFunctions.getSpannableText(
//                getString(R.string.txt_athletes_trainer),
//                "{{trainer}}",
//                athleteDataModel.
//            )
//        )
        if (!TextUtils.isEmpty(athleteDataModel.athletic_name.age)) {
            binding.clubListLayout.tvAge.setText(
                AppFunctions.getSpannableText(
                    getString(R.string.txt_athletes_age),
                    "{{age}}",
                    athleteDataModel.athletic_name.age
                )
            )
            binding.clubListLayout.tvAge.visibility = View.VISIBLE
        } else {
            binding.clubListLayout.tvAge.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(athleteDataModel.athletic_name.grade)) {
            binding.clubListLayout.tvGrade.setText(
                AppFunctions.getSpannableText(
                    getString(R.string.txt_athletes_grade),
                    "{{grade}}",
                    athleteDataModel.athletic_name.grade
                )
            )
            binding.clubListLayout.tvGrade.visibility = View.VISIBLE
        } else {
            binding.clubListLayout.tvGrade.visibility = View.GONE
        }
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

        initRecyclerView(athleteDataModel.services, athleteDataModel.user_id)
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

    private fun loadCoachabilityChart(services: AthleteDataModel) {
//        binding.clubListLayout.pieChart.clear()
        binding.clubListLayout.pieChart.setChart(null)
        val data: MutableList<DataEntry> = ArrayList()
        for (i in 0..(services.nameArr.size - 1)) {
            data.add(
                ValueDataEntry(
                    services.nameArr.get(i),
                    services.valueArr.get(i).toFloat()
                )
            )
        }
        pie.data(data)
        binding.clubListLayout.pieChart.setChart(pie)
        binding.clubListLayout.pieChart.invalidate()
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

    private fun loadServiceData(slug: String, userId: String, adapterType: Int) {
        viewModel.loadDetailAthleteList(
            requireContext(),
            slug,
            userId,
            object : AthletesViewModel.ContentFetchListener {
                override fun onFetched(anyObject: Any) {
                    if (anyObject is FormServiceModel) {
                        loadCoachData(anyObject, adapterType)
                    }
//                    binding.progressBar.visibility = View.GONE
                }
            })
//                        loadCoachData(anyData as List<Service>, adapterType)
    }


}

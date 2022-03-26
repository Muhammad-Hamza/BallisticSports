package com.example.sportsballistics.ui.dashboard.club_admin

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.sportsballistics.R
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.AthleteResponse
import com.example.sportsballistics.utils.AppConstant
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.local.StateModel
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.GenericResponseModel
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.FragmentCreateClubAdminBinding
import com.example.sportsballistics.ui.dashboard.create_athlete.CreateAthleteViewModel
import com.example.sportsballistics.utils.AppUtils.Companion.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_create_club_admin.*
import java.lang.reflect.Type

class CreateClubAdminFragment : Fragment() {
    private lateinit var binding: FragmentCreateClubAdminBinding
    lateinit var viewModel: CreateClubAdminViewModel
    var clubAdminId: String? = null
    var screenType: Int = AppConstant.INTENT_SCREEN_TYPE_ADD
    var stateArray = arrayOf("Active", "Inactive")
    val listOfState = ArrayList<StateModel>()
    val listOfClub = ArrayList<UsersItem?>()
    private var selectedStateModel: StateModel? = null
    private var selectClubModel: UsersItem? = null

    //    var imageFile = File("profPic")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_club_admin,
            container,
            false
        );
        val type: Type = object : TypeToken<List<StateModel>>() {}.type

        val list: List<StateModel> = Gson().fromJson(AppConstant.STATE_CONTENT, type)
        if (list != null && list.size > 0) {
            listOfState.addAll(list)
        }
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAssets()
        if (arguments != null && requireArguments().containsKey(AppConstant.INTENT_SCREEN_TYPE)) {
            screenType = requireArguments().getInt(
                AppConstant.INTENT_SCREEN_TYPE,
                AppConstant.INTENT_SCREEN_TYPE_ADD
            )
        }
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1)) {
            clubAdminId = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
        }
        initStatusAdapter()
        initStateAdapter()

        initClickListener()

        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT || screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            if (clubAdminId != null) {
                viewModel.viewAthlete(requireContext(), clubAdminId!!, object :
                    CreateClubAdminViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        if (anyObject is AthleteResponse) {
                            val response = anyObject as AthleteResponse
                            bindDataInUserProfile(response)
                        }
                    }

                    override fun onError(t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
        } else {
            loadClubList()
        }
        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD) {
            binding.txtTotalTrainersText.setText("Create Club Admin")
            binding.txtEdit.visibility = View.GONE
            binding.tvPasswordLabel.setText("Password*")
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            binding.txtTotalTrainersText.setText("View Club Admin Profile")
            binding.txtEdit.visibility = View.VISIBLE
            binding.tvPasswordLabel.setText("Password")
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT) {
            binding.txtTotalTrainersText.text = "Edit Club Admin"
            binding.txtEdit.visibility = View.GONE
            binding.tvPasswordLabel.setText("Password")
        }

    }

    private fun bindDataInUserProfile(athleteResponse: AthleteResponse) {
        binding.etFullName.setText(athleteResponse.userData?.fullname)
        binding.etAge.setText(athleteResponse.userData?.age)
        binding.etGrade.setText(athleteResponse.userData?.grade)
        binding.etEmail.setText(athleteResponse.userData?.email)
//        binding.etPassword.setText(athleteResponse.userData?.password)
        binding.etContact.setText(athleteResponse.userData?.contactNo)
        if (!TextUtils.isEmpty(athleteResponse.userData?.status)) {
            if (athleteResponse.userData?.status.equals(
                    "Active",
                    true
                ) || athleteResponse.userData?.status.equals("Y", true)
            ) {
                binding.etStatus.setSelection(0)
            } else {
                binding.etStatus.setSelection(1)
            }
        } else {
            binding.etStatus.setSelection(1)
        }
        binding.etAddress1.setText(athleteResponse.userData?.address)
        binding.etCity.setText(athleteResponse.userData?.city)
        for (i in 0..(listOfState.size - 1)) {
            if (listOfState.get(i).name.equals(athleteResponse.userData?.state)) {
                binding.etState.setSelection(i)
            }
        }
        binding.etZipcode.setText(athleteResponse.userData?.zipcode)
        if (!TextUtils.isEmpty(athleteResponse.userData?.clubId)) {
            binding.etClubId.setText(athleteResponse.userData?.clubId)
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            doDisableEditing(false)
        } else {
            doDisableEditing(true)
        }
        loadClubList()
    }

    private fun doDisableEditing(boolean: Boolean) {
        binding.etFullName.isEnabled = boolean
        binding.etAge.isEnabled = boolean
        binding.etGrade.isEnabled = boolean
        binding.etEmail.isEnabled = boolean
        binding.etPassword.isEnabled = boolean
        binding.etContact.isEnabled = boolean
        binding.etAddress1.isEnabled = boolean
        binding.etCity.isEnabled = boolean
        binding.etState.isEnabled = boolean
        binding.etZipcode.isEnabled = boolean
        binding.etStatus.isEnabled = boolean
        binding.etClub.isEnabled = boolean
        binding.btnSubmit.visibility = if (boolean) View.VISIBLE else View.GONE
        binding.tvCancel.visibility = if (boolean) View.VISIBLE else View.GONE
//        binding.etState.isClickable = boolean
    }

    private fun initStatusAdapter() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.listitem_spinner, stateArray)
        binding.etStatus.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
    }

    private fun initStateAdapter() {
//        val stateAdapter = StateAdapter(listOfState, requireContext())
        val newList = ArrayList<String>()
        for (i in 0..(listOfState.size - 1)) {
            newList.add(listOfState.get(i).name)
        }
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.listitem_spinner, newList)
        binding.etState.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
        binding.etState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedStateModel = listOfState.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun initClickListener() {
        binding.txtEdit.setOnClickListener {
            doDisableEditing(true)
            binding.txtTotalTrainersText.setText("Edit Club Admin")
            binding.txtEdit.visibility = View.GONE
            screenType = AppConstant.INTENT_SCREEN_TYPE_EDIT
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.llStatusDropdown.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etStatus.performClick()
            }
        }
        binding.llClub.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etClub.performClick()
            }
        }
        binding.llState.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etState.performClick()
            }
        }
        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(binding.etFullName.text.toString())) {
                showToast("First name is required")
            } else if (TextUtils.isEmpty(binding.etAge.text.toString())) {
                showToast("Age is required")
            } else if (TextUtils.isEmpty(binding.etGrade.text.toString())) {
                showToast("Club admin grade is required")
            } else if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
                showToast("Email is required")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
                showToast("Email is not valid")
            }/* else if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
                showToast("Password is required")
            }*/ else if (TextUtils.isEmpty(binding.etContact.text.toString())) {
                showToast("Contact number is required")
            } else if (TextUtils.isEmpty(binding.etStatus.selectedItem.toString())) {
                showToast("Status is required")
            } else if (TextUtils.isEmpty(binding.etAddress1.text.toString())) {
                showToast("First address is required")
            } else if (TextUtils.isEmpty(binding.etCity.text.toString())) {
                showToast("City is required")
            } else if (TextUtils.isEmpty(binding.etState.selectedItem.toString())) {
                showToast("State is required")
            } else if (TextUtils.isEmpty(binding.etZipcode.text.toString())) {
                showToast("Zip code is required")
            } else if (TextUtils.isEmpty(binding.etClub.selectedItem.toString())) {
                showToast("Club name is required")
            } else {
                if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD) {
                    if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
                        showToast("Password is required")
                    } else {
                        hitAPIRequest()
                    }
                } else {
                    hitAPIRequest()
                }
            }
        }
    }

    private fun hitAPIRequest() {
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && clubAdminId != null) {
            val password: String? =
                if (TextUtils.isEmpty(binding.etPassword.text.toString())) null else binding.etPassword.text.toString()
            viewModel.editAthlete(
                requireContext(),
                clubAdminId!!,
                binding.etFullName.text.toString(),
                binding.etAddress1.text.toString(),
                binding.etState.selectedItem.toString(),
                binding.etZipcode.text.toString(),
                binding.etCity.text.toString(),
                getStatus(),
                binding.etContact.text.toString(),
                binding.etAge.text.toString(),
                binding.etGrade.text.toString(),
                password,
                "",
                selectClubModel!!.id!!,
                AppSystem.getInstance().getCurrentUser()!!.loggedIn?.roleId.toString(),
                binding.etEmail.text.toString(),
                object :
                    CreateClubAdminViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        if (anyObject is DashboardModel) {
                            if (!anyObject.is_error) {
                                showToast(anyObject.message)
                                Navigation.findNavController(binding.root).navigateUp()
                            } else {
                                showToast(anyObject.message)
                            }
                        } else {
                            showToast("Unable to edit profile.\nPlease try again later.")
                        }
                    }

                    override fun onError(t: Throwable) {
//                        showToast(t?.localizedMessage)
                    }
                })
        } else {
            viewModel.addAthelete(
                requireContext(),
                binding.etFullName.text.toString(),
                binding.etAddress1.text.toString(),
                binding.etState.selectedItem.toString(),
                binding.etZipcode.text.toString(),
                binding.etCity.text.toString(),
                getStatus(),
                binding.etContact.text.toString(),
                binding.etAge.text.toString(),
                binding.etGrade.text.toString(),
                binding.etPassword.text.toString(),
                "",
                selectClubModel!!.id!!,
                AppConstant.ROLE_ATHLETES_PORTAL,
                binding.etEmail.text.toString(),
                object :
                    CreateAthleteViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        if (anyObject is DashboardModel) {
                            if (!anyObject.is_error) {
                                showToast(anyObject.message)
                                Navigation.findNavController(binding.root).navigateUp()
                            } else {
                                showToast(anyObject.message)
                            }
                        } else {
                            showToast("Unable to create club admin profile.\nPlease try again later.")
                        }
                    }

                    override fun onError(t: Throwable) {
                        showToast("Unable to create club admin profile.\nPlease try again later.")
                    }
                })
        }
    }

    private fun getStatus(): String {
        return if (binding.etStatus.selectedItem.toString()
                .equals("active", true)
        ) "active" else "inactive"
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(CreateClubAdminViewModel::class.java)
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
    }

    private fun loadClubList() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getClubListFromServer(
            binding.root.context,
            URLIdentifiers.CLUB_CONTENT,
            object : CreateClubAdminViewModel.ContentFetchListener {
                override fun onFetched(anyObject: Any) {
                    binding.progressBar.visibility = View.GONE
                    if (anyObject != null && anyObject is ClubResponse) {
                        if (anyObject.content != null && anyObject.content.users != null && anyObject.content.users.size > 0) {
                            loadClubContent(anyObject.content.users)
                        } else {
                            showToast("Clubs not found, please try again later")
                        }
                    }
                }

                override fun onError(t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    showToast("Club listing not found, please try again later")
                }

            })
    }

    private fun loadClubContent(users: List<UsersItem?>) {
        listOfClub.addAll(users)
        val sampleList = ArrayList<String>()

        for (i in 0..(listOfClub.size - 1)) {
            sampleList.add(listOfClub.get(i)!!.name!!)
        }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.listitem_spinner, sampleList)
//        binding.etClub.threshold = 1 //will start working from first character
        binding.etClub.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
        binding.etClub.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectClubModel = listOfClub.get(position)
                if (selectClubModel != null) {
//                    binding.etClub.setText(selectClubModel!!.name)
                    binding.etClubId.setText(selectClubModel!!.id)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        if (!TextUtils.isEmpty(binding.etClubId.text.toString())) {
            for (i in 0..(listOfClub.size - 1)) {
                if (listOfClub.get(i)!!.id!!.equals(binding.etClubId.text.toString())) {
                    selectClubModel = listOfClub.get(i)
//                    binding.etClub.setText(selectClubModel!!.name)
                    binding.etClub.setSelection(i)
                    binding.etClubId.setText(selectClubModel!!.id)
                    break
                }
            }
        }
    }

    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType
        AppConstant.changeColor(binding.txtTotalTrainersText)
        AppConstant.changeColor(binding.tvCancel)
        binding.btnSubmit.background = null
        var drawable: Drawable? = null
        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo)
                    .into(binding.imgLogo)
                drawable =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.btn_baseball)
            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo)
                    .into(binding.imgLogo)
                drawable =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.btn_baseball)
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_bg)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo)
                    .into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_qb)
            }
        }
        if (drawable != null) {
            binding.btnSubmit.background = drawable
            binding.btnSubmit.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.white
                )
            )
        }
    }
}
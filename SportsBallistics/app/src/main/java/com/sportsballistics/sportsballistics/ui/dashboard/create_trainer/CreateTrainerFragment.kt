package com.sportsballistics.sportsballistics.ui.dashboard.create_trainer

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.sportsballistics.sportsballistics.AppSystem
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.SharedPrefUtil
import com.sportsballistics.sportsballistics.data.listeners.Listeners
import com.sportsballistics.sportsballistics.data.local.StateModel
import com.sportsballistics.sportsballistics.data.remote.AthleteResponse
import com.sportsballistics.sportsballistics.data.remote.DashboardModel
import com.sportsballistics.sportsballistics.databinding.FragmentCreateTrainerBinding
import com.sportsballistics.sportsballistics.ui.dashboard.create_athlete.CreateAthleteViewModel
import com.sportsballistics.sportsballistics.utils.AppConstant
import com.sportsballistics.sportsballistics.utils.AppUtils.Companion.showToast
import java.util.regex.Pattern

class CreateTrainerFragment : Fragment() {
    private lateinit var binding: FragmentCreateTrainerBinding
    lateinit var viewModel: CreateAthleteViewModel
    var trainerId: String? = null

    var screenType: Int = AppConstant.INTENT_SCREEN_TYPE_ADD
    var stateArray = arrayOf("Active", "Inactive")
    val listOfState = ArrayList<StateModel>()
    private var selectedStateModel: StateModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_trainer, container, false);
        listOfState.addAll(AppConstant.getStateList())
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
            trainerId = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
        }
        initStateAdapter()
        initStatusAdapter()
        initClickListener()
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT || screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            if (trainerId != null) {
                viewModel.viewAthlete(requireContext(), trainerId!!, object :
                    CreateAthleteViewModel.ContentFetchListener {
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
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD) {
            binding.txtTotalTrainersText.setText("Create Trainer")
            binding.txtEdit.visibility = View.GONE
            binding.tvPasswordLabel.setText("Password*")
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            binding.txtTotalTrainersText.setText("View Trainer Profile")
            binding.txtEdit.visibility = View.VISIBLE
            binding.tvPasswordLabel.setText("Password")
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT) {
            binding.txtTotalTrainersText.setText("Edit Trainer")
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

        if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            doDisableEditing(false)
        } else {
            doDisableEditing(true)
        }
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
//        binding.etState.isEnabled = boolean
        binding.etZipcode.isEnabled = boolean
//        binding.etStatus.isEnabled = boolean
        binding.btnSubmit.visibility = if (boolean) View.VISIBLE else View.GONE
        binding.tvCancel.visibility = if (boolean) View.VISIBLE else View.GONE
//        binding.etState.isClickable = boolean
    }

    private fun initStatusAdapter() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.listitem_spinner, stateArray)
        binding.etStatus.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
    }

    private fun initClickListener() {
        binding.txtEdit.setOnClickListener {
            doDisableEditing(true)
            binding.txtTotalTrainersText.setText("Edit Trainer")
            binding.txtEdit.visibility = View.GONE
            screenType = AppConstant.INTENT_SCREEN_TYPE_EDIT
        }
        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.llStatus.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etStatus.performClick()
            }
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.llState.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etState.performClick()
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(binding.etFullName.text.toString())) {
                showToast("First name is required")
            } else if (TextUtils.isEmpty(binding.etAge.text.toString())) {
                showToast("Age is required")
            } else if (TextUtils.isEmpty(binding.etGrade.text.toString())) {
                showToast("Trainer grade is required")
            } else if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
                showToast("Email is required")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
                showToast("Email is not valid")
            } /*else if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
                showToast("Password is required")
            } */ else if (TextUtils.isEmpty(binding.etContact.text.toString())) {
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
            } else if (!Pattern.matches("[0-9]+", binding.etZipcode.text.toString()))
            {
                showToast("Zip code should only contain numbers")
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
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedStateModel =
                    listOfState.get(position)
                selectedStateModel!!.name
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun getStatus(): String {
        return if (binding.etStatus.selectedItem.toString()
                .lowercase() == "active"
        ) "active" else "inactive"
    }

    private fun hitAPIRequest() {
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && trainerId != null) {
            val password: String? =
                if (TextUtils.isEmpty(binding.etPassword.text.toString())) null else binding.etPassword.text.toString()
            viewModel.editAthlete(
                requireContext(),
                trainerId!!,
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
                AppSystem.getInstance().getCurrentUser()!!.loggedIn?.clubId.toString(),
                AppSystem.getInstance().getCurrentUser()!!.loggedIn?.roleId.toString(),
                binding.etEmail.text.toString(),
                object :
                    CreateAthleteViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        Navigation.findNavController(binding.root).navigateUp()

//                        showMessage("Athlete Added")
                    }

                    override fun onError(t: Throwable) {
//                        showMessage(t?.localizedMessage)
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
                AppSystem.getInstance().getCurrentUser()!!.loggedIn?.clubId.toString(),
                AppConstant.ROLE_TRAINER_PORTAL,
                binding.etEmail.text.toString(),
                object :
                    CreateAthleteViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        if (anyObject is DashboardModel) {
                            val obj = anyObject as DashboardModel

                            if (obj.is_error) {
                                showToast(obj.message)
                            } else {
                                Navigation.findNavController(binding.root).navigateUp()
                                showToast(obj.message)
                            }
                        }
                    }

                    override fun onError(t: Throwable) {
                        showToast(t?.localizedMessage)
                    }
                })
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(CreateAthleteViewModel::class.java)
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
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_baseball)
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
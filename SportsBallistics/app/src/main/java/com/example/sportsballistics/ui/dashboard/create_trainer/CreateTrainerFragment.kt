package com.example.sportsballistics.ui.dashboard.create_trainer

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.sportsballistics.R
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.athletes.AthleteDataModel
import com.example.sportsballistics.databinding.FragmentCreateAthleteBinding
import com.example.sportsballistics.databinding.FragmentCreateTrainerBinding
import com.example.sportsballistics.utils.AppConstant


class CreateTrainerFragment : Fragment() {
    private lateinit var binding: FragmentCreateTrainerBinding
    lateinit var viewModel: CreateTrainerViewModel
    var trainerId: String? = null

    var screenType: Int = AppConstant.INTENT_SCREEN_TYPE_ADD
    var stateArray = arrayOf("Active", "Inactive")

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
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        initClickListener()
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT || screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            if (trainerId != null) {
                viewModel.getTrainerInfo(
                    requireContext(),
                    trainerId!!,
                    object : CreateTrainerViewModel.ContentFetchListener {
                        override fun onFetched(anyObject: Any) {
                            if (anyObject is DashboardModel) {
                                bindDataInUserProfile(anyObject.data as AthleteDataModel)
                            }
                        }
                    })
            }
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD) {
            binding.txtTotalTrainersText.setText("Create Trainer")
            binding.txtEdit.visibility = View.GONE
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            binding.txtTotalTrainersText.setText("View Trainer Profile")
            binding.txtEdit.visibility = View.VISIBLE
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT) {
            binding.txtTotalTrainersText.setText("Edit Trainer")
            binding.txtEdit.visibility = View.GONE
        }
    }

    private fun bindDataInUserProfile(athleteDataModel: AthleteDataModel) {
        binding.etFullName.setText("")
        binding.etAge.setText("")
        binding.etGrade.setText("")
        binding.etEmail.setText("")
        binding.etPassword.setText("")
        binding.etContact.setText("")
        binding.etStatus.setText("")
        binding.etAddress1.setText("")
        binding.etAddress2.setText("")
        binding.etCity.setText("")
        binding.etState.setText("")
        binding.etZipcode.setText("")
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
        binding.etAddress2.isEnabled = boolean
        binding.etCity.isEnabled = boolean
        binding.etState.isEnabled = boolean
        binding.etZipcode.isEnabled = boolean
        binding.btnSubmit.visibility = if (boolean) View.VISIBLE else View.GONE
        binding.tvCancel.visibility = if (boolean) View.VISIBLE else View.GONE
//        binding.etState.isClickable = boolean
    }

    private fun initStateAdapter() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item, stateArray)
        binding.etStatus.threshold = 1 //will start working from first character
        binding.etStatus.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
    }

    private fun initClickListener() {
        binding.txtEdit.setOnClickListener {
            doDisableEditing(true)
            binding.txtTotalTrainersText.setText("Edit Trainer")
            binding.txtEdit.visibility = View.GONE
            screenType = AppConstant.INTENT_SCREEN_TYPE_EDIT
        }
        binding.etStatus.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW)
                binding.etStatus.showDropDown()
        }

        binding.llStateDropdown.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW)
                binding.etStatus.showDropDown()
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(binding.etFullName.text.toString())) {
                showMessage("First name is required")
            } else if (TextUtils.isEmpty(binding.etAge.text.toString())) {
                showMessage("Age is required")
            } else if (TextUtils.isEmpty(binding.etGrade.text.toString())) {
                showMessage("Trainer Grade is required")
            } else if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
                showMessage("Email is required")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
                showMessage("Email is not valid")
            } else if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
                showMessage("Password is required")
            } else if (TextUtils.isEmpty(binding.etContact.text.toString())) {
                showMessage("Contact Number is required")
            } else if (TextUtils.isEmpty(binding.etState.text.toString())) {
                showMessage("Status is required")
            } else if (TextUtils.isEmpty(binding.etAddress1.text.toString())) {
                showMessage("First Address is required")
            } else if (TextUtils.isEmpty(binding.etCity.text.toString())) {
                showMessage("City is required")
            } else if (TextUtils.isEmpty(binding.etState.text.toString())) {
                showMessage("State is required")
            } else if (TextUtils.isEmpty(binding.etZipcode.text.toString())) {
                showMessage("Zip Code is required")
            } else {
                hitAPIRequest()
            }
        }
    }

    private fun hitAPIRequest() {
        //TODO NEED TO ADD API REQUEST HIT.
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && trainerId != null) {
            //Create Edit Request
        } else {
//            create new add athlete request
        }
        Navigation.findNavController(binding.root).navigateUp()
    }

    private fun showMessage(content: String) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(CreateTrainerViewModel::class.java)
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
}
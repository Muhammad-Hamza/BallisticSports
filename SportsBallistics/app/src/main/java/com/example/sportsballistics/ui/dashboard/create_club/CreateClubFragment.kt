package com.example.sportsballistics.ui.dashboard.create_club

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.sportsballistics.R
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.databinding.FragmentClubBinding
import com.example.sportsballistics.databinding.FragmentCreateClubBinding
import com.example.sportsballistics.ui.dashboard.club.ClubListViewModel
import com.example.sportsballistics.utils.AppConstant
import java.lang.invoke.ConstantCallSite


class CreateClubFragment : Fragment() {

    lateinit var clubId: String
    var screenType: Int = AppConstant.INTENT_SCREEN_TYPE_ADD
    var stateArray = arrayOf("Active", "Inactive")

    lateinit var binding: FragmentCreateClubBinding
    private lateinit var viewModel: CreateClubViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_club, container, false
        );
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(AppConstant.INTENT_SCREEN_TYPE)) {
                screenType = requireArguments().getInt(AppConstant.INTENT_SCREEN_TYPE)
            }
            if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1)) {
                clubId = requireArguments().getString(AppConstant.INTENT_EXTRA_1, null)
            }
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            binding.txtTotalTrainersText.setText("View Club Profile")
            binding.txtEdit.visibility = View.VISIBLE
            doDisableEditing(false)
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT) {
            binding.txtTotalTrainersText.setText("Edit Club Profile")
            binding.txtEdit.visibility = View.GONE
            doDisableEditing(true)
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD) {
            binding.txtTotalTrainersText.setText("Add New Club")
            binding.txtEdit.visibility = View.GONE
        }
        initStateAdapter()
        binding.etStatus.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW)
                binding.etStatus.showDropDown()
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.llStateDropdown.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW)
                binding.etStatus.showDropDown()
        }
        binding.txtEdit.setOnClickListener {
            doDisableEditing(true)
            binding.txtTotalTrainersText.setText("Edit Club Profile")
            binding.txtEdit.visibility = View.GONE
            screenType = AppConstant.INTENT_SCREEN_TYPE_EDIT
        }

        binding.btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(binding.etClubName.text.toString())) {
                showMessage("Club Name is required")
            } else if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
                showMessage("Email is required")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
                showMessage("Email is not valid")
            } else if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
                showMessage("Password is required")
            } else if (TextUtils.isEmpty(binding.etAddress1.text.toString())) {
                showMessage("First Address is required")
            } else if (TextUtils.isEmpty(binding.etCity.text.toString())) {
                showMessage("City is required")
            } else if (TextUtils.isEmpty(binding.etState.text.toString())) {
                showMessage("State is required")
            } else if (TextUtils.isEmpty(binding.etZipcode.text.toString())) {
                showMessage("Zip Code is required")
            } else if (TextUtils.isEmpty(binding.etState.text.toString())) {
                showMessage("Status is required")
            } else {
                hitAPIRequest()
            }
        }
    }

    private fun hitAPIRequest() {
        showMessage("Done Adding Club")
        //TODO NEED TO ADD API REQUEST HIT.
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && clubId != null) {
            //Create Edit Request
        } else {
//            create new add athlete request
        }
        Navigation.findNavController(binding.root).navigateUp()
    }

    private fun initStateAdapter() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item, stateArray)
        binding.etStatus.threshold = 1 //will start working from first character
        binding.etStatus.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
    }

    private fun showMessage(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(CreateClubViewModel::class.java)
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

    private fun doDisableEditing(boolean: Boolean) {
        binding.etClubName.isEnabled = boolean
        binding.etEmail.isEnabled = boolean
        binding.etPassword.isEnabled = boolean
        binding.etAddress1.isEnabled = boolean
        binding.etAddress2.isEnabled = boolean
        binding.etCity.isEnabled = boolean
        binding.etState.isEnabled = boolean
        binding.etZipcode.isEnabled = boolean
        binding.btnSubmit.visibility = if (boolean) View.VISIBLE else View.GONE
        binding.tvCancel.visibility = if (boolean) View.VISIBLE else View.GONE
//        binding.etState.isClickable = boolean
    }

}
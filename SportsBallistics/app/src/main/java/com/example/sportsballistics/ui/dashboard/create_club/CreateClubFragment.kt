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
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.ViewClubResponse
import com.example.sportsballistics.databinding.FragmentClubBinding
import com.example.sportsballistics.databinding.FragmentCreateClubBinding
import com.example.sportsballistics.ui.dashboard.club.ClubListViewModel
import com.example.sportsballistics.utils.AppConstant
import java.lang.invoke.ConstantCallSite

class CreateClubFragment : Fragment()
{

    lateinit var clubId: String
    var screenType: Int = AppConstant.INTENT_SCREEN_TYPE_ADD
    var stateArray = arrayOf("Active", "Inactive")
    var isEdit = false

    lateinit var binding: FragmentCreateClubBinding
    private lateinit var viewModel: CreateClubViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_club, container, false);
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null)
        {
            if (requireArguments().containsKey(AppConstant.INTENT_SCREEN_TYPE))
            {
                screenType = requireArguments().getInt(AppConstant.INTENT_SCREEN_TYPE)
            }
            if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1))
            {
                clubId = requireArguments().getString(AppConstant.INTENT_EXTRA_1, null)
            }
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW)
        {
            binding.txtTotalTrainersText.setText("View Club Profile")
            binding.txtEdit.visibility = View.VISIBLE
            isEdit = true
            doDisableEditing(false)
        }
        else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT)
        {
            binding.txtTotalTrainersText.setText("Edit Club Profile")
            binding.txtEdit.visibility = View.GONE
            isEdit = true
            doDisableEditing(true)
        }
        else if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD)
        {
            binding.txtTotalTrainersText.setText("Add New Club")
            binding.txtEdit.visibility = View.GONE
        }
        initStateAdapter()
        binding.etStatus.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) binding.etStatus.showDropDown()
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.llStateDropdown.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) binding.etStatus.showDropDown()
        }
        binding.txtEdit.setOnClickListener {
            doDisableEditing(true)
            binding.txtTotalTrainersText.setText("Edit Club Profile")
            binding.txtEdit.visibility = View.GONE
            screenType = AppConstant.INTENT_SCREEN_TYPE_EDIT
        }

        binding.btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(binding.etClubName.text.toString()))
            {
                showMessage("Club Name is required")
            }
//            else if (TextUtils.isEmpty(binding.etEmail.text.toString()))
//            {
//                showMessage("Email is required")
//            }
//            else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches())
//            {
//                showMessage("Email is not valid")
//            }
//            else if (TextUtils.isEmpty(binding.etPassword.text.toString()))
//            {
//                showMessage("Password is required")
//            }
            else if (TextUtils.isEmpty(binding.etAddress1.text.toString()))
            {
                showMessage("First Address is required")
            }
            else if (TextUtils.isEmpty(binding.etCity.text.toString()))
            {
                showMessage("City is required")
            }
            else if (TextUtils.isEmpty(binding.etState.text.toString()))
            {
                showMessage("State is required")
            }
            else if (TextUtils.isEmpty(binding.etZipcode.text.toString()))
            {
                showMessage("Zip Code is required")
            }
            else if (TextUtils.isEmpty(binding.etState.text.toString()))
            {
                showMessage("Status is required")
            }
            else
            {

                if (!isEdit) addClub(isEdit, binding.etClubName.text.toString(), binding.etAddress1.text.toString(), binding.etState.text.toString(), binding.etZipcode.text.toString().toInt(), 10, "", binding.etCity.text.toString(), "", binding.etStatus.text.toString())
                else editClub(isEdit, binding.etClubName.text.toString(), binding.etAddress1.text.toString(), binding.etState.text.toString(), binding.etZipcode.text.toString().toInt(), 10, "", binding.etCity.text.toString(), "", binding.etStatus.text.toString())
            }
        }
    }

    private fun hitAPIRequest()
    {
        showMessage("Done Adding Club")
        //TODO NEED TO ADD API REQUEST HIT.
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && clubId != null)
        {
            //Create Edit Request
        }
        else
        {
        }
        Navigation.findNavController(binding.root).navigateUp()
    }

    private fun initStateAdapter()
    {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item, stateArray)
        binding.etStatus.threshold = 1 //will start working from first character
        binding.etStatus.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
    }

    private fun showMessage(text: String)
    {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProviders.of(this).get(CreateClubViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener
        {
            override fun dismissDialog()
            {
                binding.progressBar.visibility = View.GONE
            }

            override fun addDialog()
            {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun addErrorDialog()
            {
                binding.progressBar.visibility = View.GONE
            }

            override fun addErrorDialog(msg: String?)
            {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun doDisableEditing(boolean: Boolean)
    {
        viewModel.viewClub(requireContext(), clubId, object :
                CreateClubViewModel.ContentFetchListener
        {
            override fun onSuccess(content: ViewClubResponse)
            {
                binding.etClubName.setText(content.clubData?.name)
                binding.etAddress1.setText(content.clubData?.address)
                binding.etCity.setText(content.clubData?.city)
                binding.etState.setText(content.clubData?.state)
                binding.etZipcode.setText(content.clubData?.zipcode)
                binding.etStatus.setText(content.clubData?.status)
                binding.btnSubmit.visibility = if (boolean) View.VISIBLE else View.GONE
                binding.tvCancel.visibility = if (boolean) View.VISIBLE else View.GONE
                binding.etClubName.isEnabled = boolean
                binding.etAddress1.isEnabled = boolean
                binding.etCity.isEnabled = boolean
                binding.etState.isEnabled = boolean
                binding.etZipcode.isEnabled = boolean
                binding.etStatus.isEnabled = boolean
            }

            override fun onSuccess(content: DashboardModel)
            {
            }

            override fun onError(t: Throwable)
            {
                TODO("Not yet implemented")
            }
        })

//        binding.etState.isClickable = boolean
    }

    private fun addClub(isEdit: Boolean, name: String, address: String, state: String, zipcode: Int, limit: Int, email: String, city: String, password: String, status: String)
    {
        if (name.isNotEmpty() && address.isNotEmpty() && email.isNotEmpty() && city.isNotEmpty() && password.isNotEmpty())
        {
            viewModel.addClub(isEdit, requireContext(), name, address, state, zipcode, city, status, object :
                    CreateClubViewModel.ContentFetchListener
            {
                override fun onSuccess(content: ViewClubResponse)
                {

                }

                override fun onSuccess(content: DashboardModel)
                {
                    Toast.makeText(requireContext(), "Club added", Toast.LENGTH_SHORT).show()
                }

                override fun onError(t: Throwable)
                {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        else
        {
            validate(name, address, state, zipcode, email, city, password, status)
        }
    }

    private fun editClub(isEdit: Boolean, name: String, address: String, state: String, zipcode: Int, limit: Int, email: String, city: String, password: String, status: String)
    {
        if (name.isNotEmpty() && address.isNotEmpty())
        {
            viewModel.editClub(requireContext(), clubId, name, address, state, zipcode, city, status, object :
                    CreateClubViewModel.ContentFetchListener
            {
                override fun onSuccess(content: ViewClubResponse)
                {

                }

                override fun onSuccess(content: DashboardModel)
                {
                    Toast.makeText(requireContext(), "Club added", Toast.LENGTH_SHORT).show()
                }

                override fun onError(t: Throwable)
                {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        else
        {
            validate(name, address, state, zipcode, email, city, password, status)
        }
    }

    fun validate(name: String, address: String, state: String, zipcode: Int, email: String, city: String, password: String, status: String)
    {
        when
        {
            name.isNotEmpty() ->
            {
                Toast.makeText(requireContext(), "Please insert name", Toast.LENGTH_SHORT).show()
            }
            address.isNotEmpty() ->
            {
                Toast.makeText(requireContext(), "Please insert address", Toast.LENGTH_SHORT).show()

            }
            email.isNotEmpty() ->
            {
                Toast.makeText(requireContext(), "Please insert email", Toast.LENGTH_SHORT).show()

            }
            city.isNotEmpty() ->
            {
                Toast.makeText(requireContext(), "Please insert city", Toast.LENGTH_SHORT).show()

            }
            password.isNotEmpty() ->
            {
                Toast.makeText(requireContext(), "Please insert password", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
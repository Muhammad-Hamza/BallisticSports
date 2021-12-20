package com.example.sportsballistics.ui.dashboard.create_club

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.local.StateModel
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.ViewClubResponse
import com.example.sportsballistics.databinding.FragmentCreateClubBinding
import com.example.sportsballistics.ui.dashboard.create_club.component.StateAdapter
import com.example.sportsballistics.utils.AppConstant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class CreateClubFragment : Fragment() {

    lateinit var clubId: String
    var screenType: Int = AppConstant.INTENT_SCREEN_TYPE_ADD
    var stateArray = arrayOf("Active", "Inactive")
    var isEdit = false
    val listOfState = ArrayList<StateModel>()

    lateinit var binding: FragmentCreateClubBinding
    private lateinit var viewModel: CreateClubViewModel
    private var selectedStateModel: StateModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_club, container, false);
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
            isEdit = true
            doDisableEditing(false)
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT) {
            binding.txtTotalTrainersText.setText("Edit Club Profile")
            binding.txtEdit.visibility = View.GONE
            isEdit = true
            doDisableEditing(true)
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD) {
            binding.txtTotalTrainersText.setText("Add New Club")
            binding.txtEdit.visibility = View.GONE

        }
        initStatusAdapter()
        initStateAdapter()
        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.etStatus.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                AppConstant.showSpinnerDropdown(binding.etState)
            }
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.llStateDropdown.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                AppConstant.showSpinnerDropdown(binding.etState)
            }
        }
        binding.txtEdit.setOnClickListener {
            doDisableEditing(true)
            binding.txtTotalTrainersText.setText("Edit Club Profile")
            binding.txtEdit.visibility = View.GONE
            screenType = AppConstant.INTENT_SCREEN_TYPE_EDIT
        }

        binding.llState.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                AppConstant.showSpinnerDropdown(binding.etState)
            }
        }
        binding.etState.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                AppConstant.showSpinnerDropdown(binding.etState)
            }
        }
        binding.btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(binding.etClubName.text.toString())) {
                showMessage("Club Name is required")
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
                if (!isEdit)
                    addClub(
                        isEdit,
                        binding.etClubName.text.toString(),
                        binding.etAddress1.text.toString(),
                        binding.etState.text.toString(),
                        binding.etZipcode.text.toString().toInt(),
                        10,
                        "",
                        binding.etCity.text.toString(),
                        "",
                        binding.etStatus.text.toString()
                    )
                else
                    editClub(
                        isEdit,
                        binding.etClubName.text.toString(),
                        binding.etAddress1.text.toString(),
                        binding.etState.text.toString(),
                        binding.etZipcode.text.toString().toInt(),
                        10,
                        "",
                        binding.etCity.text.toString(),
                        "",
                        binding.etStatus.text.toString()
                    )
            }
        }
    }

    private fun hitAPIRequest() {
        showMessage("Done Adding Club")
        //TODO NEED TO ADD API REQUEST HIT.
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && clubId != null) {
            //Create Edit Request
        } else {
        }
        Navigation.findNavController(binding.root).navigateUp()
    }

    private fun initStatusAdapter() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.listitem_spinner, stateArray)
        binding.etStatus.threshold = 1 //will start working from first character
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
        binding.etState.threshold = 1 //will start working from first character
        binding.etState.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
        binding.etState.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedStateModel = listOfState.get(position)
                if (selectedStateModel != null) binding.etState.setText(selectedStateModel!!.name)
            }

        })
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
        viewModel.viewClub(requireContext(), clubId, object :
            CreateClubViewModel.ContentFetchListener {
            override fun onSuccess(content: ViewClubResponse) {
                binding.etClubName.setText(content.clubData?.name)
                binding.etAddress1.setText(content.clubData?.address)
                binding.etCity.setText(content.clubData?.city)
                binding.etState.setText(content.clubData?.state)
                binding.etZipcode.setText(content.clubData?.zipcode)
                if (content.clubData?.status.equals("Y")) binding.etStatus.setText("Active") else binding.etStatus.setText(
                    "Inactive"
                )
                binding.btnSubmit.visibility = if (boolean) View.VISIBLE else View.GONE
                binding.tvCancel.visibility = if (boolean) View.VISIBLE else View.GONE
                binding.etClubName.isEnabled = boolean
                binding.etAddress1.isEnabled = boolean
                binding.etCity.isEnabled = boolean
                binding.etState.isEnabled = boolean
                binding.etZipcode.isEnabled = boolean
                binding.etStatus.isEnabled = boolean
            }

            override fun onSuccess(content: DashboardModel) {
            }

            override fun onError(t: Throwable) {
                TODO("Not yet implemented")
            }
        })

//        binding.etState.isClickable = boolean
    }

    private fun addClub(
        isEdit: Boolean,
        name: String,
        address: String,
        state: String,
        zipcode: Int,
        limit: Int,
        email: String,
        city: String,
        password: String,
        status: String
    ) {
        if (name.isNotEmpty() && address.isNotEmpty() && city.isNotEmpty()) {
            viewModel.addClub(
                isEdit,
                requireContext(),
                name,
                address,
                state,
                zipcode,
                city,
                status,
                object :
                    CreateClubViewModel.ContentFetchListener {
                    override fun onSuccess(content: ViewClubResponse) {

                    }

                    override fun onSuccess(content: DashboardModel) {
                        Toast.makeText(requireContext(), "Club added", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun editClub(
        isEdit: Boolean,
        name: String,
        address: String,
        state: String,
        zipcode: Int,
        limit: Int,
        email: String,
        city: String,
        password: String,
        status: String
    ) {
        if (name.isNotEmpty() && address.isNotEmpty()) {
            viewModel.editClub(
                requireContext(),
                clubId,
                name,
                address,
                state,
                zipcode,
                city,
                status,
                object :
                    CreateClubViewModel.ContentFetchListener {
                    override fun onSuccess(content: ViewClubResponse) {

                    }

                    override fun onSuccess(content: DashboardModel) {
                        Toast.makeText(requireContext(), "Club added", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun validate(
        name: String,
        address: String,
        state: String,
        zipcode: Int,
        email: String,
        city: String,
        password: String,
        status: String
    ) {
        when {
            name.isNotEmpty() -> {
                Toast.makeText(requireContext(), "Please insert name", Toast.LENGTH_SHORT).show()
            }
            address.isNotEmpty() -> {
                Toast.makeText(requireContext(), "Please insert address", Toast.LENGTH_SHORT).show()
            }
            email.isNotEmpty() -> {
                Toast.makeText(requireContext(), "Please insert email", Toast.LENGTH_SHORT).show()
            }
            city.isNotEmpty() -> {
                Toast.makeText(requireContext(), "Please insert city", Toast.LENGTH_SHORT).show()
            }
            password.isNotEmpty() -> {
                Toast.makeText(requireContext(), "Please insert password", Toast.LENGTH_SHORT)
                    .show()
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
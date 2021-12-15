package com.example.sportsballistics.ui.dashboard.club_admin

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
import com.example.sportsballistics.data.remote.AthleteResponse
import com.example.sportsballistics.utils.AppConstant
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.AdapterView

import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.local.StateModel
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.FragmentCreateClubAdminBinding
import com.example.sportsballistics.ui.dashboard.create_athlete.CreateAthleteViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        }
        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD) {
            binding.txtTotalTrainersText.setText("Create Club Admin")
            binding.txtEdit.visibility = View.GONE
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            binding.txtTotalTrainersText.setText("View Club Admin Profile")
            binding.txtEdit.visibility = View.VISIBLE
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT) {
            binding.txtTotalTrainersText.text = "Edit Club Admin"
            binding.txtEdit.visibility = View.GONE
        }
    }

    private fun bindDataInUserProfile(athleteResponse: AthleteResponse) {
        binding.etFullName.setText(athleteResponse.userData?.fullname)
        binding.etAge.setText(athleteResponse.userData?.age)
        binding.etGrade.setText(athleteResponse.userData?.grade)
        binding.etEmail.setText(athleteResponse.userData?.email)
        binding.etPassword.setText(athleteResponse.userData?.password)
        binding.etContact.setText(athleteResponse.userData?.contactNo)
        binding.etStatus.setText(athleteResponse.userData?.status)
        binding.etAddress1.setText(athleteResponse.userData?.address)
        binding.etCity.setText(athleteResponse.userData?.city)
        binding.etState.setText(athleteResponse.userData?.state)
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
        binding.etState.isEnabled = boolean
        binding.etZipcode.isEnabled = boolean
        binding.btnSubmit.visibility = if (boolean) View.VISIBLE else View.GONE
        binding.tvCancel.visibility = if (boolean) View.VISIBLE else View.GONE
//        binding.etState.isClickable = boolean
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
        binding.etState.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedStateModel = listOfState.get(position)
                if (selectedStateModel != null) binding.etState.setText(selectedStateModel!!.name)
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
        binding.etStatus.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etStatus.setText("")
                Handler(Looper.myLooper()!!, object : Handler.Callback {
                    override fun handleMessage(msg: Message): Boolean {
                        return true
                    }
                }).postDelayed(object : Runnable {
                    override fun run() {
                        binding.etStatus.showDropDown()

                    }
                }, 250)
            }
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.llStateDropdown.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etStatus.setText("")
                Handler(Looper.myLooper()!!, object : Handler.Callback {
                    override fun handleMessage(msg: Message): Boolean {
                        return true
                    }
                }).postDelayed(object : Runnable {
                    override fun run() {
                        binding.etStatus.showDropDown()

                    }
                }, 250)
            }
        }
        binding.llClub.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etClub.setText("")
                Handler(Looper.myLooper()!!, object : Handler.Callback {
                    override fun handleMessage(msg: Message): Boolean {
                        return true
                    }
                }).postDelayed(object : Runnable {
                    override fun run() {
                        binding.etClub.showDropDown()

                    }
                }, 250)
            }
        }
        binding.etClub.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etClub.setText("")
                Handler(Looper.myLooper()!!, object : Handler.Callback {
                    override fun handleMessage(msg: Message): Boolean {
                        return true
                    }
                }).postDelayed(object : Runnable {
                    override fun run() {
                        binding.etClub.showDropDown()

                    }
                }, 250)
            }
        }
        binding.llState.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etState.setText("")
                Handler(Looper.myLooper()!!, object : Handler.Callback {
                    override fun handleMessage(msg: Message): Boolean {
                        return true
                    }
                }).postDelayed(object : Runnable {
                    override fun run() {
                        binding.etState.showDropDown()

                    }
                }, 250)
            }
        }
        binding.etState.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                binding.etState.setText("")
                Handler(Looper.myLooper()!!, object : Handler.Callback {
                    override fun handleMessage(msg: Message): Boolean {
                        return true
                    }
                }).postDelayed(object : Runnable {
                    override fun run() {
                        binding.etState.showDropDown()

                    }
                }, 250)
            }
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
                showMessage("Club Admin Grade is required")
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
            } else if (TextUtils.isEmpty(binding.etClub.text.toString())) {
                showMessage("Club Name is required")
            } else {
                hitAPIRequest()
            }
        }
    }

    private fun hitAPIRequest() {
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && clubAdminId != null) {
            viewModel.editAthlete(
                requireContext(),
                clubAdminId!!,
                binding.etFullName.text.toString(),
                binding.etAddress1.text.toString(),
                binding.etState.text.toString(),
                binding.etZipcode.text.toString().toInt(),
                binding.etCity.text.toString(),
                binding.etStatus.text.toString(),
                binding.etContact.text.toString(),
                binding.etAge.text.toString(),
                binding.etGrade.text.toString(),
                binding.etPassword.text.toString(),
                "",
                AppSystem.getInstance().getCurrentUser()!!.loggedIn?.clubId.toString(),
                AppSystem.getInstance().getCurrentUser()!!.loggedIn?.roleId.toString(),
                binding.etEmail.text.toString(),
                object :
                    CreateClubAdminViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        Navigation.findNavController(binding.root).navigateUp()
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
                binding.etState.text.toString(),
                binding.etZipcode.text.toString().toInt(),
                binding.etCity.text.toString(),
                binding.etStatus.text.toString(),
                binding.etContact.text.toString(),
                binding.etAge.text.toString(),
                binding.etGrade.text.toString(),
                binding.etPassword.text.toString(),
                "",
                AppSystem.getInstance().getCurrentUser()!!.loggedIn?.clubId.toString(),
                AppConstant.ROLE_ATHLETES_PORTAL,
                binding.etEmail.text.toString(),
                object :
                        CreateAthleteViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
//                        Navigation.findNavController(binding.root).navigateUp()
                    }

                    override fun onError(t: Throwable) {
//                        showMessage(t?.localizedMessage)
                    }
                })
        }
    }

    private fun showMessage(content: String) {
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
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
        viewModel.getClubListFromServer(
            binding.root.context,
            URLIdentifiers.CLUB_CONTENT,
            object : CreateClubAdminViewModel.ContentFetchListener {
                override fun onFetched(anyObject: Any) {
                    if (anyObject != null && anyObject is ClubResponse) {
                        if (anyObject.content != null && anyObject.content.users != null && anyObject.content.users.size > 0) {
                            loadClubContent(anyObject.content.users)
                        } else {
                            showMessage("Clubs not found, please try again later")
                        }
                    }
                }

                override fun onError(t: Throwable) {
                    showMessage("Club Listing not found, please try again later")
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
        binding.etClub.threshold = 1 //will start working from first character
        binding.etClub.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
        binding.etClub.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectClubModel = listOfClub.get(position)
                if (selectClubModel != null)
                    binding.etClub.setText(selectClubModel!!.name)
            }

        })


    }
}
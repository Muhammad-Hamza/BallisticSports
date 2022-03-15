package com.example.sportsballistics.ui.dashboard.create_athlete

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.AthleteResponse
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.databinding.FragmentCreateAthleteBinding
import com.example.sportsballistics.utils.AppConstant
import android.content.Intent
import android.provider.MediaStore

import android.graphics.drawable.Drawable
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.data.local.StateModel
import com.example.sportsballistics.utils.AppUtils.Companion.showToast
import com.example.sportsballistics.utils.CursorUtility
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.example.sportsballistics.utils.CursorUtility.getRealPathFromUri


class CreateAthleteFragment : Fragment() {
    private lateinit var binding: FragmentCreateAthleteBinding
    lateinit var viewModel: CreateAthleteViewModel
    var athleteId: String? = null
    val PICK_IMAGE = 1
    var screenType: Int = AppConstant.INTENT_SCREEN_TYPE_ADD
    var statusArray = arrayOf("Active", "Inactive")
    var imageFile: String? = null
    val listOfState = ArrayList<StateModel>()
    private var selectedStateModel: StateModel? = null
    lateinit var statusAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            0
        );

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_athlete, container, false);
        listOfState.addAll(AppConstant.getStateList())
        loadAssets()
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
            athleteId = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
        }
        initStatusAdapter()
        initStateAdapter()
        initClickListener()

        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT || screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            if (athleteId != null) {
                viewModel.viewAthlete(requireContext(), athleteId!!, object :
                    CreateAthleteViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        if (anyObject is AthleteResponse) {
                            val response = anyObject as AthleteResponse
                            bindDataInUserProfile(response)
                        }
                    }

                    override fun onError(t: Throwable) {
                        t.message?.let { showToast(it) }
                    }
                })
            }
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD) {
            binding.txtTotalTrainersText.setText("Create Athlete")
            binding.txtEdit.visibility = View.GONE
            binding.tvPasswordLabel.setText("Password*")
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW) {
            binding.txtTotalTrainersText.setText("View Athlete Profile")
            binding.txtEdit.visibility = View.VISIBLE
            binding.tvPasswordLabel.setText("Password")
        } else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT) {
            binding.txtTotalTrainersText.text = "Edit Athlete"
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
            if (!TextUtils.isEmpty(athleteResponse.userData?.status) && athleteResponse.userData!!.status.equals(
                    "Active", true
                ) || athleteResponse.userData?.status.equals("Y", true)
            ) {
//                statusAdapter
                binding.etStatus.setText("Active")
            } else {
                binding.etStatus.setText(
                    "Inactive"
                )
                binding.etStatus.setSelection(1)
            }
        }
        binding.etAddress1.setText(athleteResponse.userData?.address)
        binding.etCity.setText(athleteResponse.userData?.city)
        binding.etState.setText(athleteResponse.userData?.state)
        binding.etZipcode.setText(athleteResponse.userData?.zipcode)
        Glide.with(binding.root).load(athleteResponse.userData?.profileImage)
            .into(binding.imgProfile)
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


    private fun initStatusAdapter() {
        statusAdapter =
            ArrayAdapter<String>(requireContext(), R.layout.listitem_spinner, statusArray)
//        binding.etStatus.threshold = 1 //will start working from first character
        binding.etStatus.setAdapter(statusAdapter) //setting the adapter data into the AutoCompleteTextView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            if (data?.data == null) {
                //Display an error
                return
            }
//            val inputStream: InputStream? =
//                requireContext().contentResolver.openInputStream(data?.data!!)
//
//            val file = File(binding.root.context.filesDir, "/temp")
//            file.mkdirs()
//            if (File(file.path, "image.png").exists()) {
//                File(file.path, "image.png").delete()
//            }
//            val newFile = File(file.path, "image.png")
//            newFile.createNewFile()
//            if (inputStream != null) {
//                imageFile = copyStreamToFile(inputStream, newFile)
//                imageFile = newFile
//            }
            binding.imgProfile.setImageURI(data.getData())

            imageFile = getRealPathFromUri(requireContext(), data.getData())
        }
    }

    fun copyStreamToFile(inputStream: InputStream, outputFile: File): File {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }

        return outputFile
    }

    private fun initClickListener() {
        binding.backClubList.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.imgProfile.setOnClickListener {
            if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT) {
                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
                getIntent.type = "image/*"

                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"

                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

                startActivityForResult(chooserIntent, PICK_IMAGE)
            }
        }
        binding.txtEdit.setOnClickListener {
            doDisableEditing(true)
            binding.txtTotalTrainersText.setText("Edit Athlete")
            binding.txtEdit.visibility = View.GONE
            screenType = AppConstant.INTENT_SCREEN_TYPE_EDIT
        }
        binding.etStatus.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                AppConstant.showSpinnerDropdown(binding.etStatus)
            }
        }

        binding.llStateDropdown.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) {
                AppConstant.showSpinnerDropdown(binding.etStatus)
            }
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
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
            try {
                if (TextUtils.isEmpty(binding.etFullName.text.toString())) {
                    showToast("First name is required")
                } else if (TextUtils.isEmpty(binding.etAge.text.toString())) {
                    showToast("Age is required")
                } else if (TextUtils.isEmpty(binding.etGrade.text.toString())) {
                    showToast("Athlete grade is required")
                } else if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
                    showToast("Email is required")
                } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString())
                        .matches()
                ) {
                    showToast("Email is not valid")
                } else if (TextUtils.isEmpty(binding.etContact.text.toString())) {
                    showToast("Contact number is required")
                } else if (TextUtils.isEmpty(binding.etStatus.text.toString())) {
                    showToast("Status is required")
                } else if (TextUtils.isEmpty(binding.etAddress1.text.toString())) {
                    showToast("First address is required")
                } else if (TextUtils.isEmpty(binding.etCity.text.toString())) {
                    showToast("City is required")
                } else if (TextUtils.isEmpty(binding.etState.text.toString())) {
                    showToast("State is required")
                } else if (TextUtils.isEmpty(binding.etZipcode.text.toString())) {
                    showToast("Zip code is required")
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
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun hitAPIRequest() {
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && athleteId != null) {
            val password: String? =
                if (TextUtils.isEmpty(binding.etPassword.text.toString())) null else binding.etPassword.text.toString()
            viewModel.editAthlete(
                requireContext(),
                athleteId!!,
                imageFile,
                binding.etFullName.text.toString(),
                binding.etAddress1.text.toString(),
                binding.etState.text.toString(),
                binding.etZipcode.text.toString().toInt(),
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
//                        showToast("Athlete Added")
                    }

                    override fun onError(t: Throwable) {
                        showToast(t?.localizedMessage)
                    }
                })
        } else {
            viewModel.addAthelete(
                requireContext(),
                imageFile,
                binding.etFullName.text.toString(),
                binding.etAddress1.text.toString(),
                binding.etState.text.toString(),
                binding.etZipcode.text.toString().toInt(),
                binding.etCity.text.toString(),
                getStatus(),
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

    private fun getStatus(): String {
        return if (binding.etStatus.text.toString().lowercase() == "active") "Y" else "N"
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
        AppConstant.changeColor(binding.tvProfilePic)
        binding.btnSubmit.background = null
        var drawable: Drawable? = null

        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo).into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_baseball)
            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo).into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_baseball)
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_bg)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo).into(binding.imgLogo)
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
package com.example.sportsballistics.ui.dashboard.create_athlete

import android.R.attr
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.AthleteResponse
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.athletes.AthleteDataModel
import com.example.sportsballistics.databinding.FragmentCreateAthleteBinding
import com.example.sportsballistics.utils.AppConstant
import android.content.Intent
import android.provider.MediaStore

import androidx.core.app.ActivityCompat.startActivityForResult
import android.R.attr.data

import android.app.Activity
import android.os.Environment
import com.example.sportsballistics.AppSystem
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CreateAthleteFragment : Fragment()
{
    private lateinit var binding: FragmentCreateAthleteBinding
    lateinit var viewModel: CreateAthleteViewModel
    var athleteId: String? = null
    val PICK_IMAGE = 1
    var screenType: Int = AppConstant.INTENT_SCREEN_TYPE_ADD
    var stateArray = arrayOf("Active", "Inactive")
    var imageFile = File("profPic")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_athlete, container, false);
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null && requireArguments().containsKey(AppConstant.INTENT_SCREEN_TYPE))
        {
            screenType = requireArguments().getInt(AppConstant.INTENT_SCREEN_TYPE, AppConstant.INTENT_SCREEN_TYPE_ADD)
        }
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1))
        {
            athleteId = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
        }
        initStateAdapter()
        initClickListener()

        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT || screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW)
        {
            if (athleteId != null)
            {
                viewModel.viewAthlete(requireContext(), athleteId!!, object :
                        CreateAthleteViewModel.ContentFetchListener
                {
                    override fun onFetched(anyObject: Any)
                    {
                        if (anyObject is AthleteResponse)
                        {
                            val response = anyObject as AthleteResponse
                            bindDataInUserProfile(response)
                        }
                    }

                    override fun onError(t: Throwable)
                    {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_ADD)
        {
            binding.txtTotalTrainersText.setText("Create Athlete")
            binding.txtEdit.visibility = View.GONE
        }
        else if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW)
        {
            binding.txtTotalTrainersText.setText("View Athlete Profile")
            binding.txtEdit.visibility = View.VISIBLE
        }
        else if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT)
        {
            binding.txtTotalTrainersText.text = "Edit Athlete"
            binding.txtEdit.visibility = View.GONE
        }
    }

    private fun bindDataInUserProfile(athleteResponse: AthleteResponse)
    {
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
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_VIEW)
        {
            doDisableEditing(false)
        }
        else
        {
            doDisableEditing(true)
        }
    }

    private fun doDisableEditing(boolean: Boolean)
    {
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

    private fun initStateAdapter()
    {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item, stateArray)
        binding.etStatus.threshold = 1 //will start working from first character
        binding.etStatus.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == PICK_IMAGE)
        {
            if (data?.data == null)
            {
                //Display an error
                return
            }
            binding.imgProfile.setImageURI(data?.data)
            imageFile = File(data?.data!!.path)
//            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(data?.data!!)
//            val path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES);
//            imageFile = File(path ,"/"+"imageFile")
//            if (inputStream != null)
//            {
//                imageFile = copyStreamToFile(inputStream, imageFile)
//            }
        }
    }

    fun copyStreamToFile(inputStream: InputStream, outputFile: File): File
    {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true)
                {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }

        return outputFile
    }

    private fun initClickListener()
    {
        binding.imgProfile.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, PICK_IMAGE)
        }
        binding.txtEdit.setOnClickListener {
            doDisableEditing(true)
            binding.txtTotalTrainersText.setText("Edit Athlete")
            binding.txtEdit.visibility = View.GONE
            screenType = AppConstant.INTENT_SCREEN_TYPE_EDIT
        }
        binding.etStatus.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) binding.etStatus.showDropDown()
        }

        binding.llStateDropdown.setOnClickListener {
            if (screenType != AppConstant.INTENT_SCREEN_TYPE_VIEW) binding.etStatus.showDropDown()
        }

        binding.tvCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(binding.etFullName.text.toString()))
            {
                showMessage("First name is required")
            }
            else if (TextUtils.isEmpty(binding.etAge.text.toString()))
            {
                showMessage("Age is required")
            }
            else if (TextUtils.isEmpty(binding.etGrade.text.toString()))
            {
                showMessage("Athlete Grade is required")
            }
            else if (TextUtils.isEmpty(binding.etEmail.text.toString()))
            {
                showMessage("Email is required")
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches())
            {
                showMessage("Email is not valid")
            }
            else if (TextUtils.isEmpty(binding.etPassword.text.toString()))
            {
                showMessage("Password is required")
            }
            else if (TextUtils.isEmpty(binding.etContact.text.toString()))
            {
                showMessage("Contact Number is required")
            }
            else if (TextUtils.isEmpty(binding.etState.text.toString()))
            {
                showMessage("Status is required")
            }
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
            else
            {
                hitAPIRequest()
            }
        }
    }

    private fun hitAPIRequest()
    {
        if (screenType == AppConstant.INTENT_SCREEN_TYPE_EDIT && athleteId != null)
        {
            viewModel.editAthlete(requireContext(), athleteId!!, binding.etFullName.text.toString(), binding.etAddress1.text.toString(), binding.etState.text.toString(), binding.etZipcode.text.toString().toInt(), binding.etCity.text.toString(), binding.etStatus.text.toString(), binding.etContact.text.toString(), binding.etAge.text.toString(), binding.etGrade.text.toString(), binding.etPassword.text.toString(), "", AppSystem.getInstance().getCurrentUser().loggedIn?.clubId.toString(), AppSystem.getInstance().getCurrentUser().loggedIn?.roleId.toString(), binding.etEmail.text.toString(), object :
                    CreateAthleteViewModel.ContentFetchListener
            {
                override fun onFetched(anyObject: Any)
                {
//                        showMessage("Athlete Added")
                }

                override fun onError(t: Throwable)
                {
//                        showMessage(t?.localizedMessage)
                }
            })
        }
        else
        {
            viewModel.addAthelete(requireContext(), imageFile,binding.etFullName.text.toString(), binding.etAddress1.text.toString(), binding.etState.text.toString(), binding.etZipcode.text.toString().toInt(), binding.etCity.text.toString(), binding.etStatus.text.toString(), binding.etContact.text.toString(), binding.etAge.text.toString(), binding.etGrade.text.toString(), binding.etPassword.text.toString(), "", AppSystem.getInstance().getCurrentUser().loggedIn?.clubId.toString(), AppSystem.getInstance().getCurrentUser().loggedIn?.roleId.toString(), binding.etEmail.text.toString(), object :
                    CreateAthleteViewModel.ContentFetchListener
            {
                override fun onFetched(anyObject: Any)
                {
//                        showMessage("Athlete Added")
                }

                override fun onError(t: Throwable)
                {
//                        showMessage(t?.localizedMessage)
                }
            })
        }
        Navigation.findNavController(binding.root).navigateUp()
    }

    private fun showMessage(content: String)
    {
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProviders.of(this).get(CreateAthleteViewModel::class.java)
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
}
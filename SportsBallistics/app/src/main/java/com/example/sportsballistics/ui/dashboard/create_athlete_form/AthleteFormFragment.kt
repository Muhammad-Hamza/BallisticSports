package com.example.sportsballistics.ui.dashboard.create_athlete_form

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.local.AthleteFormLocalModel
import com.example.sportsballistics.data.remote.athletes.Service
import com.example.sportsballistics.data.remote.form_service.FormServiceModel
import com.example.sportsballistics.databinding.FragmentAthleteFormBinding
import com.example.sportsballistics.ui.dashboard.create_athlete_form.component.AthleteFormAdapter
import com.example.sportsballistics.ui.dashboard.create_athlete_form.component.AthleteFormViewModel
import com.example.sportsballistics.utils.AppConstant
import com.google.gson.Gson

class AthleteFormFragment : Fragment() {


    lateinit var binding: FragmentAthleteFormBinding
    lateinit var viewModel: AthleteFormViewModel
    var athleteId: String? = null
    var serviceModel: Service? = null
    lateinit var adapter: AthleteFormAdapter
    lateinit var listOfQuestion: ArrayList<AthleteFormLocalModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_athlete_form, container, false);
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1)) {
            athleteId = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
        }
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_2)) {
            val stringContent = requireArguments().getString(AppConstant.INTENT_EXTRA_2)!!
            serviceModel = Gson().fromJson(stringContent, Service::class.java)
        }
        if (!TextUtils.isEmpty(athleteId) && serviceModel != null) {
            viewModel.getServiceContent(
                binding.root.context,
                object : AthleteFormViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        if (anyObject is FormServiceModel)
                            loadUIData(anyObject)
                    }
                },
                athleteId!!,
                serviceModel!!.slug
            )
        } else {
            getBackNavigate()
        }

        binding.btnSubmit.setOnClickListener {
            if (adapter != null && context != null) {
                viewModel.submitForm(requireContext(),object :AthleteFormViewModel.ContentFetchListener{
                    override fun onFetched(anyObject: Any)
                    {
                        TODO("Not yet implemented")
                    }
                }, athleteId!!,adapter.paramMap,serviceModel!!.slug)
                showMessage("Add data inserted")
                getBackNavigate()
            }
        }
        binding.tvCancel.setOnClickListener {
            getBackNavigate()
        }
    }

    private fun loadUIData(anyObject: FormServiceModel) {
        if (anyObject.data!!.athletic_name != null && !TextUtils.isEmpty(anyObject.data.athletic_name.fullname)) {
            binding.tvInfo.setText(anyObject.data.athletic_name.fullname)
            binding.tvFormHeading.setText(serviceModel!!.name)
            listOfQuestion = ArrayList()

            for (i in 0..(anyObject.data.nameArr.size - 1)) {
                listOfQuestion.add(AthleteFormLocalModel(i, anyObject.data.nameArr[i], null, null))
            }

            adapter = AthleteFormAdapter(binding.root.context, listOfQuestion)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.setItemViewCacheSize(1000)
            binding.recyclerView.adapter = adapter
            binding.progressBar.visibility = View.GONE
        } else {
            showMessage("No Form Found")
            getBackNavigate()
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AthleteFormViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener {
            override fun dismissDialog() {
                binding.progressBar.visibility = View.GONE
            }

            override fun addDialog() {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun addErrorDialog() {
                binding.progressBar.visibility = View.GONE
                showMessage("No data found")
                getBackNavigate()
            }

            override fun addErrorDialog(msg: String?) {
                binding.progressBar.visibility = View.GONE
                showMessage(msg!!)
                getBackNavigate()
            }
        })
    }

    private fun getBackNavigate() {
        Navigation.findNavController(binding.root).navigateUp();
    }

    private fun showMessage(content: String) {
        Toast.makeText(binding.root.context, content, Toast.LENGTH_SHORT).show()
    }
}
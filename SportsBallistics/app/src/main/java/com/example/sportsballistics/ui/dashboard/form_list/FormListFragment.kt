package com.example.sportsballistics.ui.dashboard.form_list

import android.content.res.ColorStateList
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
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.athletes.Service
import com.example.sportsballistics.data.remote.service.ServiceResponseModel
import com.example.sportsballistics.databinding.FragmentFormListBinding
import com.example.sportsballistics.ui.dashboard.form_list.component.FormListAdapter
import com.example.sportsballistics.ui.dashboard.form_list.component.FormListViewModel
import com.example.sportsballistics.utils.AppConstant
import com.google.gson.Gson

class FormListFragment : Fragment() {
    lateinit var binding: FragmentFormListBinding
    lateinit var viewModel: FormListViewModel
    var athleteId: String? = null
    lateinit var adapter: FormListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_form_list, container, false);
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAssets()
        if (requireArguments().containsKey(AppConstant.INTENT_EXTRA_1)) {
            athleteId = requireArguments().getString(AppConstant.INTENT_EXTRA_1)!!
        }
        if (!TextUtils.isEmpty(athleteId)) {
            viewModel.getServiceContent(
                binding.root.context,
                object : FormListViewModel.ContentFetchListener {
                    override fun onFetched(anyObject: Any) {
                        if (anyObject is ServiceResponseModel) {
                            loadUIContent(anyObject)
                        }
                    }

                }, athleteId!!
            )
        } else {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.backClubList.setOnClickListener {
            getBackNavigate()
        }
    }

    private fun loadUIContent(anyObject: ServiceResponseModel) {
        if (anyObject.data!!.athletic_name != null && !TextUtils.isEmpty(anyObject.data.athletic_name.fullname)) {
            binding.tvInfo.setText(anyObject.data!!.athletic_name.fullname)

            adapter = FormListAdapter(
                binding.root.context,
                anyObject.data.services,
                object : FormListAdapter.OnItemClickListener {
                    override fun onEditClick(adapterType: Int, anyData: Any) {
                        if (anyData is Service) {
//                            showMessage(anyData.name)
                            val bundle = Bundle()
                            bundle.putString(AppConstant.INTENT_EXTRA_1, athleteId)
                            bundle.putString(AppConstant.INTENT_EXTRA_2, Gson().toJson(anyData))
                            Navigation.findNavController(binding.root).navigate(
                                R.id.action_formListFragment_to_athleteFormFragment,
                                bundle
                            )
                        }
                    }

                })
            binding.recyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = adapter
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(FormListViewModel::class.java)
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


    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType

        binding.progressBar.progressTintList =
            ColorStateList.valueOf(AppSystem.getInstance().getColor())

        AppConstant.changeColor(binding.tvInfo)
        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo)
                    .into(binding.imgLogo)
            }
        }
    }
}